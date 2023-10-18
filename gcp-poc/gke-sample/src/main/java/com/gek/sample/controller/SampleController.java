package com.gek.sample.controller;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class SampleController {

    @GetMapping("/currentPod")
    public V1Pod getCurrentPod() throws IOException, ApiException {
        try {
        ApiClient client = Config.fromCluster();

        CoreV1Api api =  new CoreV1Api(client);

        String podName = System.getenv("HOSTNAME");
        String namespace  = "default";

        return api.readNamespacedPod(podName, namespace, null);
    } catch(ApiException ex) {
        System.out.println(ex.getResponseBody());
        throw ex;
    }

  }

  @GetMapping("/getPodName")
  public String getPodName(){
        return System.getenv("HOSTNAME");
  }

  @GetMapping("/getAllPodName")
  public List<String> getTest() throws IOException, ApiException {
      ApiClient client = Config.fromCluster();

      CoreV1Api api =  new CoreV1Api(client);

      String namespace  = "default";

      V1PodList list = api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null,null);

      List<String> resultList = new ArrayList<>();

      for(V1Pod item : list.getItems()) {
          if(Optional.ofNullable(item.getMetadata()).isPresent()) {
              resultList.add(item.getMetadata().getName());
          }
      }

      return resultList;
    }

}
