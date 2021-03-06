import com.dropbox.core.*;
import java.io.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException, DbxException {

        // config
        final String APP_KEY = "";
        final String APP_SECRET = "";
        final String workingDirectoryPath = "";
        final String inputFileName = "";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
            Locale.getDefault().toString());
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

        // Have the user sign in and authorize your app.
        String authorizeUrl = webAuth.start();
        System.out.println("1. Go to: " + authorizeUrl);
        System.out.println("2. Click \"Allow\" (you might have to log in first)");
        System.out.println("3. Copy the authorization code.");
        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

        // This will fail if the user enters an invalid authorization code.
        DbxAuthFinish authFinish = webAuth.finish(code);
        String accessToken = authFinish.accessToken;

        DbxClient client = new DbxClient(config, accessToken);

        File inputFile = new File(workingDirectoryPath + inputFileName);
        FileInputStream inputStream = new FileInputStream(inputFile);
        try {
            DbxEntry.File uploadedFile = client.uploadFile("/uploaded-file.txt",
                DbxWriteMode.add(), inputFile.length(), inputStream);
        } finally {
            inputStream.close();
        }

        FileOutputStream outputStream = new FileOutputStream(workingDirectoryPath + "uploaded-file.txt");
        try {
            DbxEntry.File downloadedFile = client.getFile("/uploaded-file.txt", null,
                outputStream);
        } finally {
            outputStream.close();
        }
    }
}