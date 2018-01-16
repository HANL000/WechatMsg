package cn.truistic.enmicromsg.info;

public class ResultBean {


    /**
     * data : {"requestParam":{},"fileUpload":{"file":{"img2":{"originalFileName":"img2.jpg","fileName":"40c8f798af334e8a8e6abfa76e22db2c.jpg","fieldName":"img2","size":89592,"isInMemory":false,"localPath":"X:\\install\\nginx\\html\\app\\demo\\upload\\40c8f798af334e8a8e6abfa76e22db2c.jpg","contentType":"image/*","urlPath":"/media/40c8f798af334e8a8e6abfa76e22db2c.jpg"},"img1":{"originalFileName":"img1.jpg","fileName":"bb1f5c3820c34b1481f7d3341bd0f224.jpg","fieldName":"img1","size":88966,"isInMemory":false,"localPath":"X:\\install\\nginx\\html\\app\\demo\\upload\\bb1f5c3820c34b1481f7d3341bd0f224.jpg","contentType":"image/*","urlPath":"/media/bb1f5c3820c34b1481f7d3341bd0f224.jpg"}},"field":{"password":"123456789","username":"lisi"}}}
     * success : true
     * message : null
     */

    private DataBean data;
    private boolean success;
    private Object message;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * requestParam : {}
         * fileUpload : {"file":{"img2":{"originalFileName":"img2.jpg","fileName":"40c8f798af334e8a8e6abfa76e22db2c.jpg","fieldName":"img2","size":89592,"isInMemory":false,"localPath":"X:\\install\\nginx\\html\\app\\demo\\upload\\40c8f798af334e8a8e6abfa76e22db2c.jpg","contentType":"image/*","urlPath":"/media/40c8f798af334e8a8e6abfa76e22db2c.jpg"},"img1":{"originalFileName":"img1.jpg","fileName":"bb1f5c3820c34b1481f7d3341bd0f224.jpg","fieldName":"img1","size":88966,"isInMemory":false,"localPath":"X:\\install\\nginx\\html\\app\\demo\\upload\\bb1f5c3820c34b1481f7d3341bd0f224.jpg","contentType":"image/*","urlPath":"/media/bb1f5c3820c34b1481f7d3341bd0f224.jpg"}},"field":{"password":"123456789","username":"lisi"}}
         */

        private RequestParamBean requestParam;
        private FileUploadBean fileUpload;

        public RequestParamBean getRequestParam() {
            return requestParam;
        }

        public void setRequestParam(RequestParamBean requestParam) {
            this.requestParam = requestParam;
        }

        public FileUploadBean getFileUpload() {
            return fileUpload;
        }

        public void setFileUpload(FileUploadBean fileUpload) {
            this.fileUpload = fileUpload;
        }

        public static class RequestParamBean {
        }

        public static class FileUploadBean {
            /**
             * file : {"img2":{"originalFileName":"img2.jpg","fileName":"40c8f798af334e8a8e6abfa76e22db2c.jpg","fieldName":"img2","size":89592,"isInMemory":false,"localPath":"X:\\install\\nginx\\html\\app\\demo\\upload\\40c8f798af334e8a8e6abfa76e22db2c.jpg","contentType":"image/*","urlPath":"/media/40c8f798af334e8a8e6abfa76e22db2c.jpg"},"img1":{"originalFileName":"img1.jpg","fileName":"bb1f5c3820c34b1481f7d3341bd0f224.jpg","fieldName":"img1","size":88966,"isInMemory":false,"localPath":"X:\\install\\nginx\\html\\app\\demo\\upload\\bb1f5c3820c34b1481f7d3341bd0f224.jpg","contentType":"image/*","urlPath":"/media/bb1f5c3820c34b1481f7d3341bd0f224.jpg"}}
             * field : {"password":"123456789","username":"lisi"}
             */

            private FileBean file;
            private FieldBean field;

            public FileBean getFile() {
                return file;
            }

            public void setFile(FileBean file) {
                this.file = file;
            }

            public FieldBean getField() {
                return field;
            }

            public void setField(FieldBean field) {
                this.field = field;
            }

            public static class FileBean {
                /**
                 * img2 : {"originalFileName":"img2.jpg","fileName":"40c8f798af334e8a8e6abfa76e22db2c.jpg","fieldName":"img2","size":89592,"isInMemory":false,"localPath":"X:\\install\\nginx\\html\\app\\demo\\upload\\40c8f798af334e8a8e6abfa76e22db2c.jpg","contentType":"image/*","urlPath":"/media/40c8f798af334e8a8e6abfa76e22db2c.jpg"}
                 * img1 : {"originalFileName":"img1.jpg","fileName":"bb1f5c3820c34b1481f7d3341bd0f224.jpg","fieldName":"img1","size":88966,"isInMemory":false,"localPath":"X:\\install\\nginx\\html\\app\\demo\\upload\\bb1f5c3820c34b1481f7d3341bd0f224.jpg","contentType":"image/*","urlPath":"/media/bb1f5c3820c34b1481f7d3341bd0f224.jpg"}
                 */

                private Img2Bean img2;
                private Img1Bean img1;

                public Img2Bean getImg2() {
                    return img2;
                }

                public void setImg2(Img2Bean img2) {
                    this.img2 = img2;
                }

                public Img1Bean getImg1() {
                    return img1;
                }

                public void setImg1(Img1Bean img1) {
                    this.img1 = img1;
                }

                public static class Img2Bean {
                    /**
                     * originalFileName : img2.jpg
                     * fileName : 40c8f798af334e8a8e6abfa76e22db2c.jpg
                     * fieldName : img2
                     * size : 89592
                     * isInMemory : false
                     * localPath : 123
                     * contentType : image/*
                     * urlPath : /media/40c8f798af334e8a8e6abfa76e22db2c.jpg
                     */

                    private String originalFileName;
                    private String fileName;
                    private String fieldName;
                    private int size;
                    private boolean isInMemory;
                    private String localPath;
                    private String contentType;
                    private String urlPath;

                    public String getOriginalFileName() {
                        return originalFileName;
                    }

                    public void setOriginalFileName(String originalFileName) {
                        this.originalFileName = originalFileName;
                    }

                    public String getFileName() {
                        return fileName;
                    }

                    public void setFileName(String fileName) {
                        this.fileName = fileName;
                    }

                    public String getFieldName() {
                        return fieldName;
                    }

                    public void setFieldName(String fieldName) {
                        this.fieldName = fieldName;
                    }

                    public int getSize() {
                        return size;
                    }

                    public void setSize(int size) {
                        this.size = size;
                    }

                    public boolean isIsInMemory() {
                        return isInMemory;
                    }

                    public void setIsInMemory(boolean isInMemory) {
                        this.isInMemory = isInMemory;
                    }

                    public String getLocalPath() {
                        return localPath;
                    }

                    public void setLocalPath(String localPath) {
                        this.localPath = localPath;
                    }

                    public String getContentType() {
                        return contentType;
                    }

                    public void setContentType(String contentType) {
                        this.contentType = contentType;
                    }

                    public String getUrlPath() {
                        return urlPath;
                    }

                    public void setUrlPath(String urlPath) {
                        this.urlPath = urlPath;
                    }
                }

                public static class Img1Bean {
                    /**
                     * originalFileName : img1.jpg
                     * fileName : bb1f5c3820c34b1481f7d3341bd0f224.jpg
                     * fieldName : img1
                     * size : 88966
                     * isInMemory : false
                     * localPath : 123
                     * contentType : image/*
                     * urlPath : /media/bb1f5c3820c34b1481f7d3341bd0f224.jpg
                     */

                    private String originalFileName;
                    private String fileName;
                    private String fieldName;
                    private int size;
                    private boolean isInMemory;
                    private String localPath;
                    private String contentType;
                    private String urlPath;

                    public String getOriginalFileName() {
                        return originalFileName;
                    }

                    public void setOriginalFileName(String originalFileName) {
                        this.originalFileName = originalFileName;
                    }

                    public String getFileName() {
                        return fileName;
                    }

                    public void setFileName(String fileName) {
                        this.fileName = fileName;
                    }

                    public String getFieldName() {
                        return fieldName;
                    }

                    public void setFieldName(String fieldName) {
                        this.fieldName = fieldName;
                    }

                    public int getSize() {
                        return size;
                    }

                    public void setSize(int size) {
                        this.size = size;
                    }

                    public boolean isIsInMemory() {
                        return isInMemory;
                    }

                    public void setIsInMemory(boolean isInMemory) {
                        this.isInMemory = isInMemory;
                    }

                    public String getLocalPath() {
                        return localPath;
                    }

                    public void setLocalPath(String localPath) {
                        this.localPath = localPath;
                    }

                    public String getContentType() {
                        return contentType;
                    }

                    public void setContentType(String contentType) {
                        this.contentType = contentType;
                    }

                    public String getUrlPath() {
                        return urlPath;
                    }

                    public void setUrlPath(String urlPath) {
                        this.urlPath = urlPath;
                    }
                }
            }

            public static class FieldBean {
                /**
                 * password : 123456789
                 * username : lisi
                 */

                private String password;
                private String username;

                public String getPassword() {
                    return password;
                }

                public void setPassword(String password) {
                    this.password = password;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }
            }
        }
    }
}
