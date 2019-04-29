package com.example.motivationapp.motivationapp;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.model.Search;

import java.util.*;

public class GoogleImageSearch {



    private String key = "AIzaSyD5dk9YTf7Vjzakec4DnvBm3cYk0Lak_E4";

    private String cx = "006395260752177735757:qw5aujjhid4";

    private static int filecount = 0;

    private boolean enabled;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public boolean isEnabled() {
        return enabled;
    }

    private Customsearch getCustomsearch() {
        Customsearch.Builder builder = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null);
        builder.setApplicationName("motivationapp");
//	        return new Customsearch(new NetHttpTransport(), new JacksonFactory(), null);
        return builder.build();
    }




    public List<Result> searchImages(String query){



        try {
            Customsearch customsearch = getCustomsearch();

            Customsearch.Cse.List list = customsearch.cse().list(query);
            list.setKey(key);
            list.setCx(cx);
            list.setSearchType("image");
            list.setImgSize("xlarge");
            list.setImgType("photo");

            Search results = list.execute();
            List<Result> images = new ArrayList<Result>();

//	            for (Result result : results.getItems()) {
//	                Image image = new Image();
//	                image.setSource("google");
//	                image.setUrl(result.getLink());
//	                image.setTitle(result.getTitle());
//	                images.add(image);
//	            }
            return results.getItems();

        } catch (Exception e) {
            System.out.println("exception here");
            System.out.println(e);
        }
        return new ArrayList<Result>();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

//    public static void saveImage(String imageUrl) throws IOException {
//        URL url = new URL(imageUrl);
//        String fileName = url.getFile();
//
//        String destName = "images/"+(filecount++)+"image.png";
//        System.out.println(destName);
//
//        InputStream is = url.openStream();
//        OutputStream os = new FileOutputStream(destName);
//
//        byte[] b = new byte[2048];
//        int length;
//
//        while ((length = is.read(b)) != -1) {
//            os.write(b, 0, length);
//        }
//
//        is.close();
//        os.close();
//    }



//    public static void main(String[] args) throws Exception {
//        GoogleImageSearch search = new GoogleImageSearch();
//        List<Result> results = search.searchImages("apple");
//        int count = 0;
//        for(Result r:results) {
//            String storeLink = r.getImage().getThumbnailLink();
////            saveImage(storeLink);
//            System.out.println(r.getDisplayLink());
//            System.out.println(r.getTitle());
//            // all attributes:
//            System.out.println(r.toString());
//            count++;
//            if(count == 10)
//                break;
//        }
////	    	System.out.println(results.toString());
//    }



}