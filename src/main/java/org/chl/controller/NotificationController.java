package org.chl.controller;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class NotificationController {
    ApnsService service = null;
    @RequestMapping("/notification")
    public void notification(@RequestParam(value="name", defaultValue="World") String name) {

        File file = new File("Certificates.p12");
        ApnsService service = APNS.newService()
                .withCert(file.getAbsolutePath(), "iAkay2712")
                .withSandboxDestination()
                .build();

        String payload = APNS.newPayload()
                .badge(1)
                .alertBody("Can’t be simpler than this!")
 .alertTitle("test alert title").build();

        String token = "67875ccf5cebc62d06581a8ac5301fb0300c2d5762172fd5b803e4ded6e2b73d";

        service.push(token, payload);

    }
}
