package com.mapia.sns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mapia.sns.asne.instagram.InstagramLocation;

import java.util.ArrayList;

/**
 * Created by daehyun on 15. 6. 12..
 */
public class SocialPost implements Parcelable {

    public static final Parcelable.Creator<SocialPost> CREATOR
            = new Parcelable.Creator<SocialPost>() {
        public SocialPost createFromParcel(Parcel in) {
            return new SocialPost(in);
        }

        public SocialPost[] newArray(int size) {
            return new SocialPost[size];
        }
    };


    public ArrayList<SocialComment> comments;
    public String captionCreateTime;
    public String captionText;
    public SocialPerson captionFrom;
    public String captionId;

    public ArrayList<SocialPerson> likesPerson;
    public String link;
    public SocialPerson user;
    public String createTime;
    public ArrayList<String> tags;
    public InstagramLocation location;

    /*** Id of social person from chosen social network.*/
    public String id;
    /*** Name of social person from social network.*/
    public String name;
    /*** Profile picture url of social person from social network.*/
    public String avatarURL;
    /*** Profile URL of social person from social network.*/
    public String profileURL;
    /*** Email of social person from social network if exist.*/
    public String email;

    public SocialPost() {

    }

    private SocialPost(Parcel in) {
        id = in.readString();
        name = in.readString();
        avatarURL = in.readString();
        profileURL = in.readString();
        email = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(avatarURL);
        dest.writeString(profileURL);
        dest.writeString(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialPerson that = (SocialPerson) o;

        if (avatarURL != null ? !avatarURL.equals(that.avatarURL) : that.avatarURL != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (profileURL != null ? !profileURL.equals(that.profileURL) : that.profileURL != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (profileURL != null ? profileURL.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (avatarURL != null ? avatarURL.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SocialPerson{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", profileURL='" + profileURL + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
