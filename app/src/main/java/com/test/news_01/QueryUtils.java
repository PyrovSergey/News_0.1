package com.test.news_01;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.test.news_01.MainActivity.LOG_TAG;

// Класс отвечающий за запросы
public final class QueryUtils {

    private QueryUtils() {
    }

    // Обощающий публичный метод, который делает запрос на сервер, получает ответ - строку (JSON)
    // и возвращает список объектов News
    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponce = null;
        try {
            jsonResponce = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<News> news = extractFeatureFromJson(jsonResponce);
        Log.e("QueryUtils", "сработал метод fetchEarthquakeData()");

        return news;
    }

    // Возвращаем список объектов {@link Earthquake}, которые были созданы из разбора JSON-ответа.
    public static List<News> extractFeatureFromJson(String newsRequestJSON) {
        // Если переданная строка пустая или равна null - возвращаем null и дальше ничего не делаем
        if (TextUtils.isEmpty(newsRequestJSON)) {
            return null;
        }
        // Создайем пустой List, чтобы мы могли начать добавлять новости к
        List<News> news = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(newsRequestJSON);
            JSONArray articles = jsonObject.getJSONArray("articles");

            for (int i = 0; i < articles.length(); i++) {
                String name;
                String autor;
                String title;
                String description;
                String publishedAt;
                String url;
                String urlToImage;
                JSONObject currentNews = articles.optJSONObject(i);
                if (currentNews == null) {
                    continue;
                }
                JSONObject source = currentNews.optJSONObject("source");
                name = source.optString("name");
                name.toLowerCase();
                if (name == null) {
                    name = "unknown source";
                }
                autor = currentNews.optString("autor");
                if (autor == null) {
                    name = "unknown autor";
                }
                title = currentNews.optString("title");
                if (title == null) {
                    title = "Untitled";
                }
                description = currentNews.optString("description");
                if (description == null) {
                    description = "no description available";
                }
                url = currentNews.optString("url");
                if (url == null) {
                    url = "https://news.google.com/news/headlines?hl=ru&ned=ru_ru&gl=RU";
                }
                urlToImage = currentNews.optString("urlToImage");
                if (urlToImage == null) {
                    urlToImage = "https://www.google.ru/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";
                }
                String resultDate = currentNews.optString("publishedAt");
                if (resultDate == null) {
                    publishedAt = " ";
                } else {
                    publishedAt = resultDate.substring(11, 16) + "    " + resultDate.substring(0, 10);
                    publishedAt = publishedAt.replaceAll("-", ".");
                }
                news.add(new News(name, autor, title, description, publishedAt, url, urlToImage));
            }
        } catch (JSONException e) {
            Log.e("MyTAGS", "Problem parsing the earthquake JSON results", e);
        }
        return news;
    }

    // Сделайте HTTP-запрос к указанному URL-адресу и верните строку (JSON) как ответ.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Чтение строки из входящего потока, которая содержит весь ответ JSON с сервера.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    // Возвращает новый URL-объект из заданного строкового URL-адреса.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
}
