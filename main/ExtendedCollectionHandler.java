package exp4server.main;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import exp4server.frozen.Handler;
import exp4server.frozen.Request;
import exp4server.sample.*;

public class ExtendedCollectionHandler extends Handler {

  public ExtendedCollectionHandler(Socket socket) throws IOException {
      super(socket);
  }
  
  String HtmlParse(String str){
    return str.replace("&","&amp;").replace("<","&lt;").replace(">", "&gt;").replace("\"","&quot;");
  }
  
  @Override
  protected void perform(Request req) throws IOException {
      // 実装してください

      // 標準出力にメソッド，ヘッダ，ボディを出力
      outputRawLog(req);
      
      // レスポンスを返す
      String value = null;
      Data data = new Data();
      sendln("HTTP/1.0 200 OK");
      sendln("Content-Type: text/html; charset=utf-8");

      if (req.getRequestURI().equals("/")) {
        value = exp4server.sample.SampleRandom.generateRandomId();
//        db.put(value, null);
        new File("tmp/exp4server-"+value);
        sendln("Set-Cookie: key="+value);
        sendln(""); // ヘッダの終り
        sendln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        sendln("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ja-JP\" xml:lang=\"ja-JP\">");
        sendln("<head><title>アンケート</title></head>");
        sendln("<body>");
        sendln("<p>アンケートにようこそ！</p>");
        sendln("<form action=\"sex\" method=\"post\">");
        sendln("<input type=\"submit\" value=\"送信\">");
        sendln("</form>");
      }
      else if (req.getRequestURI().contains("sex")) {
        sendln(""); // ヘッダの終り
        sendln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        sendln("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ja-JP\" xml:lang=\"ja-JP\">");
        sendln("<head><title>アンケート</title></head>");
        sendln("<body>");
        sendln("<form action=\"name\" method=\"post\">");
        sendln("性別<br>");
        sendln("<input type=\"radio\" name=\"sex\" value=\"男\">男 <input type=\"radio\" name=\"sex\" value=\"女\">女<br>");
        sendln("<input type=\"submit\" value=\"送信\">");
        sendln("</form>");
      }
      else if (req.getRequestURI().contains("name")) {
        value = req.getHeaders().get("Cookie").split("=")[1];
        data.sex = req.getBody().substring(4);
        SampleSerializer.save("tmp/exp4server-"+value, data); //ファイルに保存
        sendln(""); // ヘッダの終り
        sendln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        sendln("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ja-JP\" xml:lang=\"ja-JP\">");
        sendln("<head><title>アンケート</title></head>");
        sendln("<body>");
        sendln("<form action=\"description\" method=\"post\">");
        sendln("名前<br>");
        sendln("<input type=\"text\" name=\"name\"><br>");
        sendln("<input type=\"submit\" value=\"送信\">");
        sendln("</form>");
      }
      else if (req.getRequestURI().contains("description")) {
        value = req.getHeaders().get("Cookie").split("=")[1];
        data.sex = ((Data)SampleSerializer.load("tmp/exp4server-"+value)).sex; //セッションキーからファイルを特定
        data.name = req.getBody().substring(5);
        SampleSerializer.save("tmp/exp4server-"+value, data);
        sendln(""); // ヘッダの終り
        sendln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        sendln("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ja-JP\" xml:lang=\"ja-JP\">");
        sendln("<head><title>アンケート</title></head>");
        sendln("<body>");
        sendln("<form action=\"confirm\" method=\"post\">");
        sendln("感想<br>");
        sendln("<textarea rows=\"3\" cols=\"20\" name=\"description\"></textarea><br>");
        sendln("<input type=\"submit\" value=\"送信\">");
        sendln("</form>");
      }
      else if (req.getRequestURI().contains("confirm")) {
        value = req.getHeaders().get("Cookie").split("=")[1];
        data.sex = ((Data)SampleSerializer.load("tmp/exp4server-"+value)).sex;
        data.name = ((Data)SampleSerializer.load("tmp/exp4server-"+value)).name;
        data.description = req.getBody().substring(12);
        SampleSerializer.save("tmp/exp4server-"+value, data);
        sendln(""); // ヘッダの終り
        sendln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        sendln("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ja-JP\" xml:lang=\"ja-JP\">");
        sendln("<head><title>アンケート</title></head>");
        sendln("<body>");
        sendln("<p><strong>確認</strong></p>");
        sendln("性別<p style=\"padding-left:10px;\">"+URLDecoder.decode(data.sex,"UTF-8")+"</p>");
        sendln("名前<p style=\"padding-left:10px;\">"+HtmlParse(URLDecoder.decode(data.name,"UTF-8"))+"</p>");
        sendln("感想<p style=\"padding-left:10px;\">"+HtmlParse(URLDecoder.decode(data.description,"UTF-8")).replace("\n","<br>")+"</p>");
        sendln("<form action=\"finish\" method=\"post\">");
        sendln("<input type=\"hidden\" name=\"sex\" value=\""+URLDecoder.decode(data.sex,"UTF-8")+"\">");
        sendln("<input type=\"hidden\" name=\"name\" value=\""+HtmlParse(URLDecoder.decode(data.name,"UTF-8"))+"\">");
        sendln("<input type=\"hidden\" name=\"description\" value=\""+HtmlParse(URLDecoder.decode(data.description,"UTF-8"))+"\">");
        sendln("<input type=\"submit\" value=\"送信\">");
        sendln("</form>");
      }
      else if (req.getRequestURI().contains("finish")) {
        value = req.getHeaders().get("Cookie").split("=")[1];
        new File("tmp/exp4server-"+value).delete(); //ファイルを削除
        sendln(""); // ヘッダの終り
        sendln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        sendln("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"ja-JP\" xml:lang=\"ja-JP\">");
        sendln("<head><title>アンケート</title></head>");
        sendln("<body>");
        sendln("ありがとうございました。");
      }        
      sendln("</body></html>");
   }
}
