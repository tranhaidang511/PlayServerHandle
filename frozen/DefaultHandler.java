package exp4server.frozen;

import java.io.IOException;
import java.net.Socket;



/**
 * Webクライアントと通信を行うクラス
 */
public class DefaultHandler extends Handler {
    public DefaultHandler(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    protected void perform(Request req) throws IOException {
        // 標準出力にメソッド，ヘッダ，ボディを出力
        outputRawLog(req);

        // レスポンスを返す
        sendln("HTTP/1.0 200 OK");
        sendln("Content-Type: text/html; charset=utf-8");
        sendln(""); // ヘッダの終り

        sendln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        sendln("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ja-JP\" xml:lang=\"ja-JP\">");
        sendln("<head><title>It works!</title></head>");
        sendln("<body>");
        sendln("<p>あなたの予想に反して、このページが見えているでしょうか?</p>");
        sendln("</body></html>");
    }
}
