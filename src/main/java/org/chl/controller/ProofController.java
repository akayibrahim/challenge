package org.chl.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.chl.model.Member;
import org.chl.model.Proof;
import org.chl.repository.ProofRepository;
import org.chl.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProofController {
    @Autowired
    private GridFsOperations gridOperations;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private ProofRepository proofRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ErrorService errorService;
    @Autowired
    ProofService proofService;

    // this variable is used to store ImageId for other actions like: findOne or delete
    private String imageFileId = "";

    @Transactional
    @RequestMapping(value = "/uploadImage")
    public String uploadImage(MultipartFile file, String challengeId, String memberId) throws IOException, java.lang.Exception {
        try {
            proofService.uploadImage(file, challengeId, memberId);
        } catch (java.lang.Exception e) {
            logError(challengeId, memberId, "uploadImage", e, "memberId=" + memberId + "&challengeId=" + challengeId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/downloadImage", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] downloadImage(String challengeId, String memberId) throws IOException, java.lang.Exception {
        try {
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
        } catch (java.lang.Exception e) {
            logError(challengeId, memberId, "downloadImage", e, "memberId=" + memberId + "&challengeId=" + challengeId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/downloadProofImageByObjectId", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] downloadProofImageByObjectId(String objectId) throws IOException, java.lang.Exception {
        try {
            GridFSFile file =  gridOperations.findOne(new Query(Criteria.where("_id").is(objectId)));
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
        } catch (java.lang.Exception e) {
            logError(null, null, "downloadProofImageByObjectId", e, "objectId=" + objectId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getProofInfoListByChallenge")
    public @ResponseBody List<Proof> getProofInfoListByChallenge(String challengeId) throws IOException, java.lang.Exception {
        try {
            Iterable<Proof>  proofIterable = proofRepository.findByChallengeId(challengeId, new Sort(Sort.Direction.DESC,"insertDate"));
            List<Proof> proofs = new ArrayList<>();
            proofIterable.forEach(file -> {
                Proof proof = new Proof();
                proof.setChallengeId(challengeId);
                proof.setProofObjectId(file.getProofObjectId());
                proof.setMemberId(file.getMemberId());
                Member member = memberService.getMemberInfo(file.getMemberId());
                proof.setName(member.getName() + " " + member.getSurname());
                proof.setFbID(member.getFacebookID());
                proofs.add(proof);
            });
            return proofs;
        } catch (java.lang.Exception e) {
            logError(challengeId, null, "getProofInfoListByChallenge", e, "challengeId=" + challengeId);
        }
        return null;
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    @Transactional
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

    @Transactional
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

    @Transactional
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

    @Transactional
    @RequestMapping("/api/delete/image")
    public String deleteFile(){
        // delete image via id
        gridOperations.delete(new Query(Criteria.where("_id").is(imageFileId)));
        return "Done";
    }

    private void logError(String challengeId, String memberId, String serviceURL, java.lang.Exception e, String inputs) throws java.lang.Exception {
        errorService.logError(challengeId,memberId,serviceURL,e,inputs);
    }
}
