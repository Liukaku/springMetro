package github.com.liukaku.metrolinkApi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootApplication
@RestController
public class MetrolinkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetrolinkApiApplication.class, args);
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/stop/{id}")
	public String getStopById(@PathVariable("id") String id) throws IOException {
		String urlString = String.format("https://api.tfgm.com/odata/Metrolinks(%s)", id);
		URL fetchUrl = new URL(urlString);
		HttpURLConnection goGet = (HttpURLConnection) fetchUrl.openConnection();
		goGet.setRequestMethod("GET");

		Map<String, String> headers = new HashMap<>();
		Dotenv dotenv = Dotenv.configure().directory("src/assets/").filename("env").load();
		String secret = dotenv.get("API_KEY");

		System.out.println(secret);

		goGet.setRequestProperty("Ocp-Apim-Subscription-Key", secret);
		goGet.setRequestProperty("Content-Type", "application/json; charset=utf-8");

		int responseCode = goGet.getResponseCode();

		BufferedReader in = new BufferedReader(
		new InputStreamReader(goGet.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);

		}
		in.close();
		goGet.disconnect();

		System.out.println(response);

		return response.toString();
	}

	@GetMapping("/test/{id}")
	public List<Item> testEndpoint(@PathVariable("id") int id) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File is = new File("src/db.json");
//		InputStream is = Item.class.getResourceAsStream("src/db.json");

		List<Item> testObj = mapper.readValue(is, new TypeReference<List<Item>>() {
		});

		System.out.println(testObj);

		List<Item> testRes = testObj.stream().filter(obj -> obj.uid == id).collect(Collectors.toList());

		return testRes;

	}

}
