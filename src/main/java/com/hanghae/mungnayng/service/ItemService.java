package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.image.Image;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ChartResponseDto;
import com.hanghae.mungnayng.domain.item.dto.ItemMainResponseDto;
import com.hanghae.mungnayng.domain.item.dto.ItemRequestDto;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.domain.zzim.Zzim;
import com.hanghae.mungnayng.repository.*;
import com.hanghae.mungnayng.util.TimeUtil;
import com.hanghae.mungnayng.util.Validator;
import com.hanghae.mungnayng.util.aws.S3uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final S3uploader s3uploader;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final ZzimRepository zzimRepository;
    private final MemberRepository memberRepository;
    private final Validator validator;

    /* 상품 등록 */
    public ItemResponseDto createItem(UserDetails userDetails, ItemRequestDto itemRequestDto) throws IOException {
        validator.validateCreateItemInput(userDetails, itemRequestDto);    /* 상품 등록 메소드 유효성 검사 메소드화 */

        Item item = Item.builder()
                .title(itemRequestDto.getTitle())
                .content(itemRequestDto.getContent())
                .nickname(((UserDetailsImpl) userDetails).getMember().getNickname())
                .petCategory(itemRequestDto.getPetCategory())
                .itemCategory(itemRequestDto.getItemCategory())
                .location(itemRequestDto.getLocation())
                .purchasePrice(itemRequestDto.getPurchasePrice())
                .sellingPrice(itemRequestDto.getSellingPrice())
                .build();
        itemRepository.save(item);

        /* itemRequestDto에서 받아온 값 중 item과 multipartFile을 따로 저장 */
        List<MultipartFile> multipartFileList = itemRequestDto.getMultipartFileList();
        if (multipartFileList != null) {
            for (MultipartFile multipartFile : multipartFileList) {
                String url = s3uploader.Uploader(multipartFile);
                Image image = Image.builder()
                        .imgUrl(url)
                        .item(item)
                        .build();
                imageRepository.save(image);
            }
        }
        return buildItemResponseDto(userDetails, item);
    }

    /* 전체 상품 조회(MainPage) */
    @Transactional(readOnly = true)
    public List<ItemMainResponseDto> getAllItem(Pageable pageable) {
        Page<Item> itemList = itemRepository.findAll(pageable);
        List<ItemMainResponseDto> itemMainResponseDtoList = new ArrayList<>();

        int lastData = itemRepository.lastData();   /* FE 무한스크롤 위한 마지막 게시글 확인 */

        for (Item item : itemList) {
            itemMainResponseDtoList.add(
                    buildItemMainResponseDto(lastData, item)
            );
        }
        return itemMainResponseDtoList;
    }

    /* 카테고리에 따른 상품 조회(MainPage/이중 카테고리) */
    @Transactional(readOnly = true)
    public List<ItemMainResponseDto> getItemByTwoCategory(String petCategory, String itemCategory, Pageable pageable) {
        validator.validateItemCategory(itemCategory);    /* 상품 카테고리 유효성 검사 */
        validator.validatePetCategory(petCategory);    /* 펫 카테고리 유효성 검사 */

        Page<Item> itemList = itemRepository.getAllItemListByTwoCategory(petCategory, itemCategory, pageable);
        List<ItemMainResponseDto> itemMainResponseDtoList = new ArrayList<>();

        int lastData = itemRepository.lastDataTwoCategory(petCategory, itemCategory);   /* FE 무한스크롤 위한 마지막 게시글 확인 */

        for (Item item : itemList) {
            itemMainResponseDtoList.add(
                    buildItemMainResponseDto(lastData, item)
            );
        }
        return itemMainResponseDtoList;
    }

    /* 카테고리에 따른 상품 조회(MainPage/단일 카테고리 - petCategory) */
    public List<ItemMainResponseDto> getItemByPetCategory(String petCategory, Pageable pageable) {
        validator.validatePetCategory(petCategory);    /* 펫 카테고리 유효성 검사 */

        Page<Item> itemList = itemRepository.getAllItemListByPetCategry(petCategory, pageable);
        List<ItemMainResponseDto> itemMainResponseDtoList = new ArrayList<>();

        int lastData = itemRepository.lastDataPetCategory(petCategory); /* FE 무한스크롤 위한 마지막 게시글 확인 */

        for (Item item : itemList) {
            itemMainResponseDtoList.add(
                    buildItemMainResponseDto(lastData, item)
            );
        }
        return itemMainResponseDtoList;
    }

    /* 카테고리에 따른 상품 조회(MainPage/단일 카테고리 - itemCategory) */
    public List<ItemMainResponseDto> getItemByItemCategory(String itemCategory, Pageable pageable) {
        validator.validateItemCategory(itemCategory);    /* 상품 카테고리 유효성 검사 */

        Page<Item> itemList = itemRepository.getAllItemListByItemCategory(itemCategory, pageable);
        List<ItemMainResponseDto> itemMainResponseDtoList = new ArrayList<>();

        int lastData = itemRepository.lastDataItemCategory(itemCategory); /* FE 무한스크롤 위한 마지막 게시글 확인 */

        for (Item item : itemList) {
            itemMainResponseDtoList.add(
                    buildItemMainResponseDto(lastData, item)
            );
        }
        return itemMainResponseDtoList;
    }

    /* 단일 상품 조회(DetailPage) */
    @Transactional(readOnly = true)
    public ItemResponseDto getItem(UserDetails userDetails, Long itemId) {
        Item item = validator.validateItemExistence(itemId);    /* 상품 존재 여부 유효성 검사 및 반환 */

        return buildItemResponseDto(userDetails, item);
    }

    /* 조회수 증가(단일 상품 조회 시) */
    @Transactional
    public void addViewCnt(Long itemId) {
        itemRepository.addViewCnt(itemId);
    }

    /* 상품 수정 - detail */
    @Transactional
    public ItemResponseDto updateItem(UserDetails userDetails, Long itemId, ItemRequestDto itemRequestDto) throws IOException {
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */
        Item item = validator.validateItemExistence(itemId);    /* 상품 존재 여부 유효성 검사 및 반환 */
        validator.validateEqualUser(userDetails, item);    /* 작성자와 조회자 일치 여부 유효성 검사 */

        item.update(itemRequestDto);

        List<MultipartFile> multipartFileList = itemRequestDto.getMultipartFileList();

        if (multipartFileList != null) {
            for (MultipartFile multipartFile : multipartFileList) {
                String url = s3uploader.Uploader(multipartFile);
                Image image = Image.builder()
                        .imgUrl(url)
                        .item(item)
                        .build();
                imageRepository.save(image);
            }
        }

        return buildItemResponseDto(userDetails, item);
    }

    /* 상품 삭제 - detail */
    @Transactional
    public void deleteItem(UserDetails userDetails, Long itemId) {
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */
        Item item = validator.validateItemExistence(itemId);    /* 상품 존재 여부 유효성 검사 및 반환 */
        validator.validateEqualUser(userDetails, item);    /* 작성자와 조회자 일치 여부 유효성 검사 */

        itemRepository.delete(item);
    }

    /* 내가 등록한 상품 조회(MyPage) */
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getMyItem(UserDetails userDetails) {
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */

        List<Item> itemList = itemRepository.getAllItemByNickname(userDetails.getUsername());
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();

        for (Item item : itemList) {
            itemResponseDtoList.add(
                    buildItemResponseDto(userDetails, item)
            );
        }
        return itemResponseDtoList;
    }

    /* 마이페이지 차트 호출 */
    @Transactional(readOnly = true)
    public List<ChartResponseDto> getMyChart(UserDetails userDetails) {
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */

        List<ChartResponseDto> chartResponseDtoList = new ArrayList<>();

        int sumMyItemsPrice = 0;    /* 자기 등록 상품 가격 총합 */
        List<Item> itemList = itemRepository.getAllItemByNickname(userDetails.getUsername());
        if (!itemList.isEmpty()) {
            sumMyItemsPrice = itemRepository.getFirstItemsPriceSum(userDetails.getUsername());
        }
        chartResponseDtoList.add(
                ChartResponseDto.builder()
                        .name("A")
                        .price(sumMyItemsPrice)
                        .build()
        );

        int sumMyItemsPriceSold = 0;    /* 판매 완료된 자기 등록 상품 가격 총합 */
        List<Item> seconditemList = itemRepository.getAllSoldItemByNickname(userDetails.getUsername());
        if (!seconditemList.isEmpty()) {
            sumMyItemsPriceSold = itemRepository.getSecondItemsPriceSum(userDetails.getUsername());
        }
        chartResponseDtoList.add(
                ChartResponseDto.builder()
                        .name("B")
                        .price(sumMyItemsPriceSold)
                        .build()
        );

        int sumPriceThatIZzimed = 0;    /* 내가 찜한 상품 가격 총합 */
        List<Item> thirdItemList = itemRepository.getAllItemListByZzimedId(userDetails.getUsername());
        if (!thirdItemList.isEmpty()) {
            sumPriceThatIZzimed = itemRepository.getThirdItemsPriceSum(userDetails.getUsername());
        }
        chartResponseDtoList.add(
                ChartResponseDto.builder()
                        .name("C")
                        .price(sumPriceThatIZzimed)
                        .build()
        );
        return chartResponseDtoList;
    }

    /* 마이페이지 - 내가 조회한 상품 리스트 호출 */
    public List<ItemMainResponseDto> getItemList(UserDetails userDetails, Map<String, String> data) {
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */

        List<ItemMainResponseDto> itemMainResponseDtoList = new ArrayList<>();
        int lastData = 0;   /* lastData가 필요없는 메소드이기에 임시값 부여 */

        if (!data.get("cookies").isEmpty()) {
            String cookies = String.valueOf(data.get("cookies"));   /* Header에서 클라이언트로부터 넘겨받은 쿠키 내용을 꺼내옴 */
            String cookiesExceptItemId = cookies.replace("itemId", "");

            String[] stringArray = cookiesExceptItemId.split(",");
            List<String> stringList = Arrays.asList(stringArray);

            for (String string : stringList) {
                String intStr = string.replaceAll("[^0-9]", "");
                Item item = itemRepository.findById(Long.parseLong(intStr)).orElseThrow(
                        () -> new IllegalArgumentException("최근 조회한 상품이 없습니다.")
                );
                itemMainResponseDtoList.add(
                        buildItemMainResponseDto(lastData, item)
                );
            }
        }
        return itemMainResponseDtoList;
    }

    /* Detail 페이지용 ResponseDto build */
    private ItemResponseDto buildItemResponseDto(UserDetails userDetails, Item item) {

        int commentCnt = commentRepository.countByItem_Id(item.getId());
        Long averagePrice = itemRepository.getAveragePrice(item.getItemCategory());    /* 해당 item이 속한 itemCategory 상품의 평균가격 도출 */

        /* 해당 item의 이미지 호출 */
        List<Image> imageList = imageRepository.findAllByItemId(item.getId());
        List<String> imgUrlList = new ArrayList<>();
        for (Image image : imageList) {
            System.out.println();
            imgUrlList.add(image.getImgUrl());
        }

        /* IsZzimed - 사용자가 찜한 상품인지 아닌지 확인 */
        boolean isZzimed = false;
        if (userDetails != null) {
            Optional<Zzim> zzim = zzimRepository.findByItemIdAndZzimedBy(item.getId(), userDetails.getUsername());
            if (zzim.isPresent()) isZzimed = true;
        }

        Member member = memberRepository.findByNickname(item.getNickname());     /* 상품 게시자 정보 가져오기 */

        return ItemResponseDto.builder()
                .id(item.getId())
                .IsMine(userDetails != null && item.getNickname().equals(userDetails.getUsername()))
                .nickname(item.getNickname())
                .title(item.getTitle())
                .content(item.getContent())
                .petCategory(item.getPetCategory())
                .itemCategory(item.getItemCategory())
                .itemImgs(imgUrlList)
                .location(item.getLocation())
                .zzimCnt(item.getZzimCnt())
                .commentCnt(commentCnt)
                .viewCnt(item.getViewCnt())
                .purchasePrice(item.getPurchasePrice())
                .sellingPrice(item.getSellingPrice())
                .averagePrice(averagePrice)
                .IsComplete(item.isComplete())
                .IsZzimed(isZzimed)
                .memberId(member.getMemberId())    /* 상품 게시자의 memberId */
                .time(TimeUtil.convertLocaldatetimeToTime(item.getCreatedAt()))
                .build();
    }

    /* 공통작업 - Main 페이지용 ResponseDto build */
    private ItemMainResponseDto buildItemMainResponseDto(int lastData, Item item) {
        boolean isLastData = false;
        if (item.getId() == lastData) {
            isLastData = true;
        }

        /* 해당 item의 이미지 호출 */
        List<Image> imageList = imageRepository.findAllByItemId(item.getId());
        List<String> imgUrlList = new ArrayList<>();
        for (Image image : imageList) {
            System.out.println();
            imgUrlList.add(image.getImgUrl());
        }

        return ItemMainResponseDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .petCategory(item.getPetCategory())
                .itemCategory(item.getItemCategory())
                .itemImgs(imgUrlList)
                .location(item.getLocation())
                .zzimCnt(item.getZzimCnt())
                .viewCnt(item.getViewCnt())
                .sellingPrice(item.getSellingPrice())
                .IsComplete(item.isComplete())
                .time(TimeUtil.convertLocaldatetimeToTime(item.getCreatedAt()))
                .lastData(isLastData)
                .build();
    }
}