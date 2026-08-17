package com.velogexpress.service;

public interface R2Service {

    /**
     * Uploads bytes to the configured R2 bucket under the given object key
     * and returns the public URL the object can be reached at.
     */
    String upload(byte[] data, String key, String contentType);

    /**
     * Builds the public URL for an existing object key without uploading anything.
     */
    String publicUrl(String key);
}
