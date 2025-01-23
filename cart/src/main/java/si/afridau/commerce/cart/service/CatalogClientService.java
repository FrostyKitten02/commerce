package si.afridau.commerce.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import si.afridau.commerce.cart.catalogclient.GetProductResDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CatalogClientService {

    @Value("${http://localhost:8001/api}")
    private String url;
    public GetProductResDto getProducts(List<UUID> ids, String auth) throws IOException {
        StringBuilder fullUrl = new StringBuilder(url);
        fullUrl.append("/products");

        if (ids == null || ids.isEmpty()) {
            return null;
        }

        for (int i = 0 ; i < ids.size() ; i++) {
            if (i == 0) {
                fullUrl.append("?ids=").append(ids.get(0).toString());
            }
            fullUrl.append("&ids=").append(ids.get(i));
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(fullUrl.toString())
                .get()
                .header("Authorization", "Bearer " + auth)
                .header("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        ObjectMapper objectMapper = new ObjectMapper();
        GetProductResDto res = objectMapper.readValue(response.body().string(), GetProductResDto.class);
        return res;
    }

}
