package com.imooc.demo;

import java.util.List;

public class LessonResult {
    private int mStatus;
    private List<Lesson> mLessons;

    @Override
    public String toString() {
        return "LessonResult{" +
                "mStatus=" + mStatus +
                ", mLessons=" + mLessons +
                '}';
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public List<Lesson> getmLessons() {
        return mLessons;
    }

    public void setmLessons(List<Lesson> mLessons) {
        this.mLessons = mLessons;
    }

    public static class Lesson{
        private int mId;
        private int mLearnerNum;
        private String mSmallPictureUrl;
        private String mBigPictureUrl;
        private String mDescription;

        @Override
        public String toString() {
            return "Lesson{" +
                    "mId=" + mId +
                    ", mLearnerNum='" + mLearnerNum + '\'' +
                    ", mSmallPictureUrl='" + mSmallPictureUrl + '\'' +
                    ", mBigPictureUrl='" + mBigPictureUrl + '\'' +
                    ", mDescription='" + mDescription + '\'' +
                    '}';
        }

        public int getmId() {
            return mId;
        }

        public void setmId(int mId) {
            this.mId = mId;
        }

        public int getmLearnerNum() {
            return mLearnerNum;
        }

        public void setmLearnerNum(int mLearnerNum) {
            this.mLearnerNum = mLearnerNum;
        }

        public String getmSmallPictureUrl() {
            return mSmallPictureUrl;
        }

        public void setmSmallPictureUrl(String mSmallPictureUrl) {
            this.mSmallPictureUrl = mSmallPictureUrl;
        }

        public String getmBigPictureUrl() {
            return mBigPictureUrl;
        }

        public void setmBigPictureUrl(String mBigPictureUrl) {
            this.mBigPictureUrl = mBigPictureUrl;
        }

        public String getmDescription() {
            return mDescription;
        }

        public void setmDescription(String mDescription) {
            this.mDescription = mDescription;
        }
    }
}
