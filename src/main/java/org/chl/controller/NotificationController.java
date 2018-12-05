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
                .withAppleDestination(true)
                .build();

        String payload = APNS.newPayload()
                .badge(1)
                .alertBody("Can’t be simpler than this!")
 .alertTitle("test alert title").build();

        String token = "0475446453005a9bae085867d17846c1ee9032cd296dcb0a1f964200df39ab4c";

        service.push(token, payload);

    }
}
