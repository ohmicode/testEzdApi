import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class ApiRunnable implements Runnable {

    private static final String DEFAULT_PROFILE_ID  = "5563037";

    private String api;
    private CountDownLatch barrier;

    public ApiRunnable(String api, CountDownLatch barrier) {
        this.api = api;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            barrier.await();
            requestApi(api);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void requestApi(String api) throws IOException {
        System.out.println("Connecting... " + api);

        URL url = new URL(api);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Profile-Id", DEFAULT_PROFILE_ID);
        con.setRequestProperty("Profile", "{\"userProfileId\": " + DEFAULT_PROFILE_ID + "}");
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        System.out.println(api + " returned " + status);

        con.disconnect();
    }
}
