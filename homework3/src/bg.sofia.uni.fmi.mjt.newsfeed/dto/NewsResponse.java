package bg.sofia.uni.fmi.mjt.newsfeed.dto;

import com.google.gson.annotations.SerializedName;

public class NewsResponse {
    @SerializedName("totalResults")
    int totalResults;

    public NewsResponse() {
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
