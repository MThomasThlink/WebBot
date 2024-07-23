
package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateContentBody {

    @SerializedName("system_instruction")
    @Expose
    private SystemInstruction systemInstruction;

    @SerializedName("contents")
    @Expose
    private List<Content> contents;
    @SerializedName("safety_settings")
    @Expose
    private SafetySettings safetySettings;
    @SerializedName("generation_config")
    @Expose
    private GenerationConfig generationConfig;

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public SafetySettings getSafetySettings() {
        return safetySettings;
    }

    public void setSafetySettings(SafetySettings safetySettings) {
        this.safetySettings = safetySettings;
    }

    public GenerationConfig getGenerationConfig() {
        return generationConfig;
    }

    public void setGenerationConfig(GenerationConfig generationConfig) {
        this.generationConfig = generationConfig;
    }

    public SystemInstruction getSystemInstruction() {
        return systemInstruction;
    }

    public void setSystemInstruction(SystemInstruction systemInstruction) {
        this.systemInstruction = systemInstruction;
    }
    
    
    public static String objToJSON (GenerateContentBody pRT)
    {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setPrettyPrinting().create();
        String resp = gson.toJson(pRT);
      //System.out.println(resp);
        return resp;
    }
}
