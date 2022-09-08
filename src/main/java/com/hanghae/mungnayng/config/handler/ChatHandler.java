package com.hanghae.mungnayng.config.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> list = new ArrayList<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : " + payload);

        for (WebSocketSession sess : list) {
            sess.sendMessage(message);
        }
    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        list.add(session);

        log.info(session + " 클라이언트 접속");
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info(session + " 클라이언트 접속 해제");
        list.remove(session);
    }

    /*이미지 전송*/

//    @Override
//    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
//        //바이너리 메시지 발송
//        ByteBuffer byteBuffer = message.getPayload();
//        String fileName = "temp.jpg";
//        File dir = new File(FILE_UPLOAD_PATH);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        File file = new File(FILE_UPLOAD_PATH, fileName);
//        FileOutputStream out = null;
//        FileChannel outChannel = null;
//        try {
//            byteBuffer.flip(); //byteBuffer를 읽기 위해 세팅
//            out = new FileOutputStream(file, true); //생성을 위해 OutputStream을 연다.
//            outChannel = out.getChannel(); //채널을 열고
//            byteBuffer.compact(); //파일을 복사한다.
//            outChannel.write(byteBuffer); //파일을 쓴다.
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (outChannel != null) {
//                    outChannel.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        byteBuffer.position(0); //파일을 저장하면서 position값이 변경되었으므로 0으로 초기화한다.
//        //파일쓰기가 끝나면 이미지를 발송한다.
//        HashMap<String, Object> temp = rls.get(fileUploadIdx);
//        for (String k : temp.keySet()) {
//            if (k.equals("roomNumber")) {
//                continue;
//            }
//            WebSocketSession wss = (WebSocketSession) temp.get(k);
//            try {
//                wss.sendMessage(new BinaryMessage(byteBuffer)); //초기화된 버퍼를 발송한다.
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}