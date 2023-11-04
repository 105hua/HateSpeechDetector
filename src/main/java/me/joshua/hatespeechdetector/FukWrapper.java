/* Licensed under GNU General Public License v3.0 */
package me.joshua.hatespeechdetector;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * The FukWrapper class is used to query Fuk.AI in order to analyze text for hate speech. It works
 * through sending an HTTP Request to the services detect-hatespeech endpoint and returns the
 * probability of the input text being hatespeech in the form of a probability percentage.
 *
 * @author 105hua
 * @version 1.0
 */
public class FukWrapper {

  // TODO Make configurable
  private static final String authToken = "";

  /**
   * Analyzes the input text for hatespeech.
   *
   * @param inputText The text to analyze.
   * @return The probability of the input text being hate speech in the form of a percentage. If a
   *     percentage cannot be obtained, -1.0 will be returned.
   */
  public static double analyzeText(String inputText) {
    URL requestUrl;
    try {
      requestUrl =
          new URL(
              "https://fuk.ai/detect-hatespeech/?input="
                  + URLEncoder.encode(inputText, StandardCharsets.UTF_8));
      HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Authorization", "Token " + authToken);
      // TODO Make configurable.
      connection.setConnectTimeout(5000);
      connection.setReadTimeout(5000);
      connection.setInstanceFollowRedirects(false);
      int responseStatus = connection.getResponseCode();
      BufferedReader inputReader =
          new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder contentBuilder = new StringBuilder();
      String inputLine;
      while ((inputLine = inputReader.readLine()) != null) {
        contentBuilder.append(inputLine);
      }
      inputReader.close();
      connection.disconnect();
      if (responseStatus == 200) {
        String jsonString = contentBuilder.toString();
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonObject resultObject = jsonObject.get("result").getAsJsonObject();
        double textProbability = resultObject.get("probability").getAsDouble();
        return textProbability;
      } else {
        return -1.0;
      }
    } catch (Exception exception) {
      HateSpeechDetector.pluginLogger.warning("Error occured when analyzing text.");
      HateSpeechDetector.pluginLogger.warning(exception.getMessage());
      return -1.0;
    }
  }
}
