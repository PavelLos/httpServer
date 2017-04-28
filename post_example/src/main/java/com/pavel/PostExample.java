package com.pavel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PostExample {
    private static PostExample instance = new PostExample();
    private PrintStream printStream;
    private PrintStream exeptionStream;

    private PostExample() {
        printStream = new PrintStream(System.out);
        exeptionStream = new PrintStream(System.err);
    }

    public static PostExample getInstance() {
        return instance;
    }

    public void createDocument(String path, String documentText) {
        StringBuilder document = new StringBuilder();
        String fileString = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            while (true) {
                try {
                    fileString = reader.readLine();
                    if (fileString == null)
                        break;
                    document.append(fileString);
                    if (fileString.equals("<body>"))
                        document.append(createTable(documentText));
                } catch (IOException e) {
                    createExeption(e.toString());
                }
            }
        } catch (FileNotFoundException e) {
            createExeption(e.toString());
        }
        sendDocument(document);
    }

    private String createTable(String documentText) {
        StringBuilder body = new StringBuilder();
        List<String> headers = new ArrayList<>();
        List<String> values = new ArrayList<>();
        String[] strings = documentText.split("&");
        for (String str : strings) {
            String[] mes = str.split("=");
            headers.add(mes[0]);
            values.add(mes[1]);
        }
        body.append("<table border=\"1\">");
        body.append("<tr>\n" +
                "    <th>Head</th>\n" +
                "    <th>Value</th>\n" +
                "   </tr>");
        for(int i = 0; i<headers.size(); i++){
            body.append("<tr><td>");
            body.append(headers.get(i));
            body.append("</td><td>");
            body.append(values.get(i));
            body.append("</td></tr>");
        }
        body.append("</table>");
        return body.toString();
    }

    public void createExeption(String e){
        StringBuilder document = new StringBuilder();
        document.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Show Message</title>\n" +
                "</head>\n" +
                "<body>\n");

        document.append(e);

        document.append("</body>\n" +
                "</html>");
        sendDocument(document);
    }

    private void sendDocument(StringBuilder document) {
        printStream.print(document);
    }

}
