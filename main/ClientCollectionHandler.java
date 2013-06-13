package exp4server.main;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;

import exp4server.frozen.Handler;
import exp4server.frozen.Request;

/**
 * Webクライアントと通信を行うクラス
 */
public class ClientCollectionHandler extends Handler {
    
    public ClientCollectionHandler(Socket socket) throws IOException {
        super(socket);
    }
    
    //特定の文字をエスケープする
    String HtmlParse(String str){
      return str.replace("&","&amp;").replace("<","&lt;").replace(">", "&gt;").replace("\"","&quot;");
    }
    
    @Override
    protected void perform(Request req) throws IOException {
        // 書き換えてください

        // 標準出力にメソッド，ヘッダ，ボディを出力
        outputRawLog(req);

        // レスポンスを返す
        sendln("HTTP/1.0 200 OK");
        sendln("Content-Type: text/html; charset=utf-8");
        sendln(""); // ヘッダの終り

        sendln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        sendln("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ja-JP\" xml:lang=\"ja-JP\">");
        sendln("<head><title>アンケート</title></head>");
        sendln("<body>");
        
        //req に応じて異なる画面を出力する
        if (req.getRequestURI().equals("/")) {
          sendln("<p>アンケートにようこそ！</p>");
          sendln("<form action=\"sex\" method=\"post\">");
          sendln("<input type=\"submit\" value=\"送信\">");
          sendln("</form>");
        }
        else if (req.getRequestURI().contains("sex")) {
          sendln("<form action=\"name\" method=\"post\">");
          sendln("性別<br>");
          sendln("<input type=\"radio\" name=\"sex\" value=\"男\">男 <input type=\"radio\" name=\"sex\" value=\"女\">女<br>");
          sendln("<input type=\"submit\" value=\"送信\">");
          sendln("</form>");
        }
        else if (req.getRequestURI().contains("name")) {
          sendln("<form action=\"description\" method=\"post\">");
          sendln("名前<br>");
          sendln("<input type=\"hidden\" name=\"sex\" value=\""+URLDecoder.decode(req.getBody().substring(4),"UTF-8")+"\">");
          sendln("<input type=\"text\" name=\"name\"><br>");
          sendln("<input type=\"submit\" value=\"送信\">");
          sendln("</form>");
        }
        else if (req.getRequestURI().contains("description")) {
          sendln("<form action=\"confirm\" method=\"post\">");
          sendln("感想<br>");
          sendln("<input type=\"hidden\" name=\"sex\" value=\""+URLDecoder.decode(req.getBody().split("&")[0].substring(4),"UTF-8")+"\">");
          sendln("<input type=\"hidden\" name=\"name\" value=\""+HtmlParse(URLDecoder.decode(req.getBody().split("&")[1].substring(5),"UTF-8"))+"\">");
          sendln("<textarea rows=\"3\" cols=\"20\" name=\"description\"></textarea><br>");
          sendln("<input type=\"submit\" value=\"送信\">");
          sendln("</form>");
        }
        else if (req.getRequestURI().contains("confirm")) {
          String sex = URLDecoder.decode(req.getBody().split("&")[0].substring(4),"UTF-8");
          String name = HtmlParse(URLDecoder.decode(req.getBody().split("&")[1].substring(5),"UTF-8"));
          String description = HtmlParse(URLDecoder.decode(req.getBody().split("&")[2].substring(12),"UTF-8"));
          sendln("<strong>確認</strong><br>");
          sendln("性別<p style=\"padding-left:10px;\">"+sex+"</p>");
          sendln("名前<p style=\"padding-left:10px;\">"+name+"</p>");
          sendln("感想<p style=\"padding-left:10px;\">"+description.replace("\n","<br>")+"</p>");//改行を<br>にする
          sendln("<form action=\"finish\" method=\"post\">");
          sendln("<input type=\"hidden\" name=\"sex\" value=\""+sex+"\">");
          sendln("<input type=\"hidden\" name=\"name\" value=\""+name+"\">");
          sendln("<input type=\"hidden\" name=\"description\" value=\""+description+"\">");
          sendln("<input type=\"submit\" value=\"送信\">");
          sendln("</form>");
        }
        else if (req.getRequestURI().contains("finish")) {
          sendln("ありがとうございました。");
        }
        sendln("</body></html>");
    }
}