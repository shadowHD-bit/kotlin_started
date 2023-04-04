package com.example.lab9_krivikov

import org.json.JSONObject
import java.io.Serializable

class GitProjectData() : Serializable {
    var name: String = "";
    var avatarURL: String = "";
    var description: String = "";
    var projectURL: String = "";
    var watchersCount: Int = 0;
    var size: Int = 0;
    var language: String = "";
    var createdAt: String = "";
    var updatedAt: String = "";
    var license: String = "";

    constructor(json: JSONObject) : this() {
        this.name = json.getString("full_name")
        this.avatarURL = json.getJSONObject("owner").getString("avatar_url")
        this.description = json.getString("description")
        this.projectURL = json.getString("html_url")
        this.watchersCount = json.getInt("watchers")
        this.size = json.getInt("size")
        this.language = json.getString("language")
        this.createdAt = json.getString("created_at")
        this.updatedAt = json.getString("updated_at")
        this.updatedAt = json.getString("updated_at")
        json.optJSONObject("license")?.let {
            this.license = it.getString("name")
        }
    }
}