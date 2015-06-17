package com.mapia.sns.asne.instagram;

import android.os.Parcel;
import android.os.Parcelable;

import com.mapia.sns.model.SocialComment;
import com.mapia.sns.model.SocialPost;

import java.util.ArrayList;

/**
 * Created by daehyun on 15. 6. 12..
 */
public class InstagramPost extends SocialPost implements Parcelable {

    public static final Parcelable.Creator<InstagramPerson> CREATOR
            = new Parcelable.Creator<InstagramPerson>() {
        public InstagramPerson createFromParcel(Parcel in) {
            return new InstagramPerson(in);
        }

        public InstagramPerson[] newArray(int size) {
            return new InstagramPerson[size];
        }
    };


    public ArrayList<SocialComment> comments;
    public String captionCreateTime;
    public String captionText;
    public InstagramPerson captionFrom;
    public String captionId;
    public ArrayList<InstagramPerson> likesPerson;
    public String link;
    public InstagramPerson user;
    public String createTime;
    public String imageLow;
    public String imageThumbnail;
    public String imageStandard;
    public ArrayList<String> tags;
    public String id;
    public InstagramLocation location;





    /*** Bio of social person*/
    public String bio;
    /*** Website of social person from user contacts*/
    public String website;
    /*** Full name of social person*/
    public String fullName;
    /*** Count of social person's media*/
    public int media;
    /*** Count of social person's followers*/
    public int followedBy;
    /*** Count of social person's follows*/
    public int follows;

    public InstagramPost() {

    }

    protected InstagramPost(Parcel in) {
        bio = in.readString();
        website = in.readString();
        fullName = in.readString();
        media = in.readInt();
        followedBy = in.readInt();
        follows = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bio);
        dest.writeString(website);
        dest.writeString(fullName);
        dest.writeInt(media);
        dest.writeInt(followedBy);
        dest.writeInt(follows);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstagramPerson)) return false;
        if (!super.equals(o)) return false;

        InstagramPerson that = (InstagramPerson) o;

        if (followedBy != that.followedBy) return false;
        if (follows != that.follows) return false;
        if (media != that.media) return false;
        if (bio != null ? !bio.equals(that.bio) : that.bio != null) return false;
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null)
            return false;
        if (website != null ? !website.equals(that.website) : that.website != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (bio != null ? bio.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + media;
        result = 31 * result + followedBy;
        result = 31 * result + follows;
        return result;
    }

    @Override
    public String toString() {
        return "InstagramPerson{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", profileURL='" + profileURL + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", website='" + website + '\'' +
                ", fullName='" + fullName + '\'' +
                ", media=" + media +
                ", followedBy=" + followedBy +
                ", follows=" + follows +
                '}';
    }
}

