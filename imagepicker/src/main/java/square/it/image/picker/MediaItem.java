package square.it.image.picker;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import square.it.image.picker.activities.MediaPickerActivity;
import square.it.image.picker.utils.MediaUtils;

/**
 * @author TUNGDX
 */

/**
 * Contains information of photo or video that selected and return back in
 * {@link MediaPickerActivity}
 */
public class MediaItem implements Parcelable {
    public static final int PHOTO = 1;
    private Uri uriCropped;
    private Uri uriOrigin;

    /**
     * @param mediaType Whether {@link #PHOTO}
     * @param uriOrigin {@link Uri} of media item.
     */
    public MediaItem(int mediaType, Uri uriOrigin) {
        this.uriOrigin = uriOrigin;
    }

    public Uri getUriCropped() {
        return uriCropped;
    }

    public void setUriCropped(Uri uriCropped) {
        this.uriCropped = uriCropped;
    }

    public Uri getUriOrigin() {
        return uriOrigin;
    }

    public void setUriOrigin(Uri uriOrigin) {
        this.uriOrigin = uriOrigin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (this.uriCropped == null) {
            dest.writeString(null);
        } else {
            dest.writeString(this.uriCropped.toString());
        }
        if (this.uriOrigin == null) {
            dest.writeString(null);
        } else {
            dest.writeString(this.uriOrigin.toString());
        }
    }

    public MediaItem(Parcel in) {
        String crop = in.readString();
        if (!TextUtils.isEmpty(crop))
            this.uriCropped = Uri.parse(crop);
        String origin = in.readString();
        if (!TextUtils.isEmpty(origin))
            this.uriOrigin = Uri.parse(origin);
    }

    public static final Creator<MediaItem> CREATOR = new Creator<MediaItem>() {

        @Override
        public MediaItem[] newArray(int size) {
            return new MediaItem[size];
        }

        @Override
        public MediaItem createFromParcel(Parcel source) {
            return new MediaItem(source);
        }
    };

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((uriCropped == null) ? 0 : uriCropped.hashCode());
        result = prime * result
                + ((uriOrigin == null) ? 0 : uriOrigin.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MediaItem other = (MediaItem) obj;
        if (uriCropped == null) {
            if (other.uriCropped != null)
                return false;
        } else if (!uriCropped.equals(other.uriCropped))
            return false;
        if (uriOrigin == null) {
            if (other.uriOrigin != null)
                return false;
        } else if (!uriOrigin.equals(other.uriOrigin))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MediaItem [type=" + ", uriCropped=" + uriCropped
                + ", uriOrigin=" + uriOrigin + "]";
    }

    /**
     * @param context
     * @return Path of origin file.
     */
    public String getPathOrigin(Context context) {
        return getPathFromUri(context, uriOrigin);
    }

    /**
     * @param context
     * @return Path of cropped file.
     */
    public String getPathCropped(Context context) {
        return getPathFromUri(context, uriCropped);
    }

    private String getPathFromUri(Context context, Uri uri) {
        if (uri == null)
            return null;
        String scheme = uri.getScheme();
        if (scheme.equals(ContentResolver.SCHEME_FILE)) {
            return uri.getPath();
        } else if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            return MediaUtils.getRealImagePathFromURI(context.getContentResolver(), uri);

        }
        return uri.toString();
    }
}