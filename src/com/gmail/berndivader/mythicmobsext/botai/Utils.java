package com.gmail.berndivader.mythicmobsext.botai;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class Utils {
    
    public static String parametersToWWWFormURLEncoded(Map<String,String>parameters) throws Exception {
        StringBuilder s=new StringBuilder();
        for (Map.Entry<String,String> parameter:parameters.entrySet()) {
            if (s.length()>0) s.append("&");
            s.append(URLEncoder.encode(parameter.getKey(),"UTF-8"));
            s.append("=");
            s.append(URLEncoder.encode(parameter.getValue(),"UTF-8"));
        }
        return s.toString();
    }
    
    public static String md5(String input) throws Exception {
        MessageDigest md5=MessageDigest.getInstance("MD5");
        md5.update(input.getBytes("UTF-8"));
        BigInteger hash=new BigInteger(1,md5.digest());
        return String.format("%1$032X",hash);
    }

    public static String toAcceptLanguageTags(Locale...l1) {
        if (l1.length==0) return "";
        float f1=1f/(float)l1.length;
        float f2=1f;
        StringBuilder sb1=new StringBuilder();
        for (int i1=0;i1<l1.length;i1++) {
            Locale locale = l1[i1];
            if (sb1.length()>0)
            	sb1.append(", ");
            if (!locale.getCountry().equals("")) {
            	sb1.append(locale.getLanguage()).append("-").append(locale.getCountry());
            	sb1.append(";q="+f2);
            	sb1.append(", ");
            	sb1.append(locale.getLanguage());
            	sb1.append(";q="+(f2-0.01));
            } else {
            	sb1.append(locale.getLanguage());
            	sb1.append(";q="+f2);
            }
            f2-=f1;
        }
        return sb1.toString();
    }

    public static String request(String s1,Map<String,String>m1,Map<String,String>m2,Map<String,String>m3) throws Exception {
        HttpURLConnection connection=(HttpURLConnection) new URL(s1).openConnection();
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36");
        if (m1!=null) {
            for (Map.Entry<String,String>header:m1.entrySet()) {
                connection.setRequestProperty(header.getKey(),header.getValue());
            }
        }
        if (m2!=null&&!m2.isEmpty()) {
            StringBuilder ch1=new StringBuilder();
            for (String cookie:m2.values()) {
                if (ch1.length()>0) {
                	ch1.append(";");
                }
                ch1.append(cookie);
            }
            connection.setRequestProperty("Cookie",ch1.toString());
        }
        connection.setDoInput(true);
        if (m3!=null&&!m3.isEmpty()) {
            connection.setDoOutput(true);
            OutputStreamWriter osw=new OutputStreamWriter(connection.getOutputStream());
            osw.write(parametersToWWWFormURLEncoded(m3));
            osw.flush();
            osw.close();
        }
        if (m2!=null) {
            for (Map.Entry<String,List<String>>m4:connection.getHeaderFields().entrySet()) {
                if (m4!=null&&m4.getKey()!=null&&m4.getKey().equalsIgnoreCase("Set-Cookie")) {
                    for (String header:m4.getValue()) {
                        for (HttpCookie hc1:HttpCookie.parse(header)) {
                        	m2.put(hc1.getName(),hc1.toString());
                        }
                    }
                }
            }
        }
        Reader r=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringWriter w=new StringWriter();
        char[]arr1=new char[1024];
        int n=0;
        while((n=r.read(arr1))!=-1) {
            w.write(arr1,0,n);
        }
        r.close();
        return w.toString();
    }
    
    public static String xPathSearch(String s1,String s2) throws Exception {
        DocumentBuilder db1=DocumentBuilderFactory.newInstance().newDocumentBuilder();
        XPath xp1=XPathFactory.newInstance().newXPath();
        XPathExpression xPathExpression=xp1.compile(s2);
        Document doc1=db1.parse(new ByteArrayInputStream(s1.getBytes("UTF-8")));
        String s3=(String)xPathExpression.evaluate(doc1, XPathConstants.STRING);
        return s3==null?"":s3.trim();
    }
    
    public static String stringAtIndex(String[]arr1,int i1) {
        if (i1>=arr1.length) return "";
        return arr1[i1];
    }
}