import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String courseURL = "https://www.udemy.com/course/learn-flutter-dart-to-build-ios-android-apps/";
        String proxyURL = "https://cors-anywhere.herokuapp.com/";



        Document doc = null;
        try {
            // Getting necessary page;
            doc = Jsoup.connect(courseURL).referrer(proxyURL).get();

            // Getting the list of all free lectures;
            Elements freeLectures =  doc.select(".lecture-container--preview");

            // Retrieving video title and length from each free lecture and adding videos to collection
            List<Video> videos = new ArrayList <>();
            for (Element lecture: freeLectures) {
                String videoName = lecture.select(".title").text();
                String videoLength = lecture.select(".details .content-summary").text();
                videos.add(new Video(videoName, videoLength));
            }

            // Ascending sort of free videos by length
            videos.sort(new Comparator <Video>() {
                @Override
                public int compare(Video v1, Video v2) {
                    int firstLength = v1.getTimeInSeconds();
                    int secondLength = v2.getTimeInSeconds();
                    if (firstLength > secondLength){
                        return 1;
                    } else if (firstLength < secondLength){
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            videos.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Video {

        private String name;
        private String time;
        private int timeInSeconds;

        public Video (String name, String time){
            this.name = name;
            this.time = time;
            this.timeInSeconds = convertToSeconds(this.time);
        }

        private int convertToSeconds(String time){
            String [] timeSplit = time.split(":");

            if (timeSplit.length == 3){
                return Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);
            } else {
                return Integer.parseInt(timeSplit[0]) * 60 + Integer.parseInt(timeSplit[1]);
            }


        }

        public int getTimeInSeconds() {
            return timeInSeconds;
        }

        @Override
        public String toString() {
            return "" + this.name + " - " + this.time;
        }
    }
}
