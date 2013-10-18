/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.eclipse.jgit.lib.ObjectId;

/**
 *
 * @author U292156
 */
public class RepoEntry extends Object {

    public RepoEntry() {
    }
    
        private String path;

    /**
     * Get the value of path
     *
     * @return the value of path
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the value of path
     *
     * @param path new value of path
     */
    public void setPath(String path) {
        this.path = path;
    }

        private ObjectId objectId;

    /**
     * Get the value of objectId
     *
     * @return the value of objectId
     */
    public ObjectId getObjectId() {
        return objectId;
    }

    /**
     * Set the value of objectId
     *
     * @param objectId new value of objectId
     */
    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

        private int commitTime;

    /**
     * Get the value of commitTime
     *
     * @return the value of commitTime
     */
    public int getCommitTime() {
        return commitTime;
    }

    /**
     * Set the value of commitTime
     *
     * @param commitTime new value of commitTime
     */
    public void setCommitTime(int commitTime) {
        this.commitTime = commitTime;
    }

    public RepoEntry(String path, ObjectId objectId, int commitTime) {
        this.path = path;
        this.objectId = objectId;
        this.commitTime = commitTime;
    }

}
