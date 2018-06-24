package org.chl.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
public class ProofController {
    @Autowired
    GridFsOperations gridOperations;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    GridFsTemplate gridFsTemplate;

    // this variable is used to store ImageId for other actions like: findOne or delete
    private String imageFileId = "";

    @RequestMapping(value = "/uploadImage")
    public String uploadImage(MultipartFile file, String challengeId, String memberId) throws IOException {
        // Define metaData
        DBObject metaData = new BasicDBObject();
        metaData.put("challengeId", challengeId);
        metaData.put("memberId", memberId);
        InputStream iamgeStream = new FileInputStream("/Users/iakay/Downloads/multiply.png");

        InputStream inputStream = file.getInputStream();
        metaData.put("type", "image");

        imageFileId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType(), metaData).toString();
        return "Done " + imageFileId;
    }

    @RequestMapping(value = "/downloadImage", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] downloadImage(String challengeId, String memberId) throws IOException {
        GridFSFile file =  gridOperations.findOne(new Query(Criteria.where("metadata.challengeId").is(challengeId).andOperator(Criteria.where("metadata.memberId").is(memberId))));
        byte[] bytesToWriteTo = null;
        if (file != null) {
            GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(file.getObjectId());
            int fileLength = (int) downloadStream.getGridFSFile().getLength();
            bytesToWriteTo = new byte[fileLength];
            int streamDownloadPosition = 0;
            while(streamDownloadPosition != -1) {
                streamDownloadPosition += downloadStream.read(bytesToWriteTo, streamDownloadPosition, fileLength);
            }
            downloadStream.close();
        }
        return bytesToWriteTo;
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    @RequestMapping("/api/save")
    public String saveFiles() throws FileNotFoundException {
        // Define metaData
        DBObject metaData = new BasicDBObject();
        metaData.put("organization", "JavaSampleApproach");
        /**
         * 1. save an image file to MongoDB
         */
        // Get input file
        InputStream iamgeStream = new FileInputStream("/Users/iakay/Downloads/multiply.png");
        metaData.put("type", "image");
        // Store file to MongoDB
        imageFileId = gridOperations.store(iamgeStream, "multiply.png", "image/png", metaData).toString();
        System.out.println("ImageFileId = " + imageFileId);
        /**
         * 2. save text files to MongoDB
         */
        // change metaData
        metaData.put("type", "data");
        // Store files to MongoDb
        gridOperations.store(new FileInputStream("/Users/iakay/Documents/Dockers NOTES"), "Dockers NOTES", "text/plain", metaData);
        // gridOperations.store(new FileInputStream("D:\\JSA\\text-2.txt"), "text-2.txt", "text/plain", metaData);
        return "Done";
    }

    @RequestMapping("/api/retrieve/imagefile")
    public String retrieveImageFile() throws IOException {
        // read file from MongoDB
        // GridFSDBFile imageFile = gridOperations.findOne(new Query(Criteria.where("_id").is(imageFileId)));
        GridFSFile imageFile = gridOperations.findOne(new Query(Criteria.where("metadata.challengeId").is("5b2cdaf41cb199833bc830ca").where("metadata.memberId").is("5b1a97bbcb353e79ca335a38")));
        // Save file back to local disk

        //imageFile.writeTo("D:\\JSA\\retrieve\\jsa-logo.png");
        //System.out.println("Image File Name:" + imageFile.getFilename());
        return "Done";
    }

    @RequestMapping("/api/retrieve/textfiles")
    public String retrieveTextFiles(){
        /**
         * get all data files then save to local disk
         */
        // Retrieve all data files
        GridFSFindIterable textFiles = gridOperations.find(new Query(Criteria.where("metadata.type").is("data")));
        // Save all back to local disk
        /*
        textFiles.forEach(file->{
            try {
                String fileName = file.getFilename();
                //file.writeTo("D:\\JSA\\retrieve\\"+ fileName);
                System.out.println("Text File Name: " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        */
        return "Done";
    }

    @RequestMapping("/api/delete/image")
    public String deleteFile(){
        // delete image via id
        gridOperations.delete(new Query(Criteria.where("_id").is(imageFileId)));
        return "Done";
    }
}