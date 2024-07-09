import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.log4j.Logger;

import javax.annotation.processing.Generated;

public class CrptApi {
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private final AtomicInteger counter;
    private long windowStartTime;
    private final static String POST_URL = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    private final static String FILE_PATH = "src/main/resources/document.json";
    private static final Logger LOGGER = Logger.getLogger(CrptApi.class);
    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        if (requestLimit < 0)
            throw new IllegalArgumentException("requestLimit must be positive");

        this.windowStartTime = System.currentTimeMillis();
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        this.counter = new AtomicInteger();
    }

    public synchronized void createDocument(Object document, String signature) throws IOException, ParseException, InterruptedException {
        tryAcquire();

        final HttpPost httpPost = new HttpPost(POST_URL);
        final ObjectMapper objectMapper = new ObjectMapper();
        final StringEntity entity = new StringEntity(objectMapper.writeValueAsString(document));

        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Signature", signature);

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(httpPost)) {
            final String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");

            LOGGER.info("Response from client: " + responseStr);
        }
    }

    private synchronized void tryAcquire() throws InterruptedException {
        long currentTime = System.currentTimeMillis();

        if (currentTime - windowStartTime >= timeUnit.toMillis(1)) {
            counter.set(0);
            windowStartTime = currentTime;
        }

        if (counter.get() >= requestLimit) {
            wait(timeUnit.toMillis(1) - (currentTime - windowStartTime));
            counter.set(0);
            windowStartTime = System.currentTimeMillis();
        }
        counter.incrementAndGet();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "participantInn"
    })
    @Generated("jsonschema2pojo")
    static class Description {
        @JsonProperty("participantInn")
        private String participantInn;

        @JsonProperty("participantInn")
        public String getParticipantInn() {
            return participantInn;
        }

        @JsonProperty("participantInn")
        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "certificate_document",
            "certificate_document_date",
            "certificate_document_number",
            "owner_inn",
            "producer_inn",
            "production_date",
            "tnved_code",
            "uit_code",
            "uitu_code"
    })
    @Generated("jsonschema2pojo")
    static class Product {
        @JsonProperty("certificate_document")
        private String certificateDocument;
        @JsonProperty("certificate_document_date")
        private String certificateDocumentDate;
        @JsonProperty("certificate_document_number")
        private String certificateDocumentNumber;
        @JsonProperty("owner_inn")
        private String ownerInn;
        @JsonProperty("producer_inn")
        private String producerInn;
        @JsonProperty("production_date")
        private String productionDate;
        @JsonProperty("tnved_code")
        private String tnvedCode;
        @JsonProperty("uit_code")
        private String uitCode;
        @JsonProperty("uitu_code")
        private String uituCode;

        @JsonProperty("certificate_document")
        public String getCertificateDocument() {
            return certificateDocument;
        }

        @JsonProperty("certificate_document")
        public void setCertificateDocument(String certificateDocument) {
            this.certificateDocument = certificateDocument;
        }

        @JsonProperty("certificate_document_date")
        public String getCertificateDocumentDate() {
            return certificateDocumentDate;
        }

        @JsonProperty("certificate_document_date")
        public void setCertificateDocumentDate(String certificateDocumentDate) {
            this.certificateDocumentDate = certificateDocumentDate;
        }

        @JsonProperty("certificate_document_number")
        public String getCertificateDocumentNumber() {
            return certificateDocumentNumber;
        }

        @JsonProperty("certificate_document_number")
        public void setCertificateDocumentNumber(String certificateDocumentNumber) {
            this.certificateDocumentNumber = certificateDocumentNumber;
        }

        @JsonProperty("owner_inn")
        public String getOwnerInn() {
            return ownerInn;
        }

        @JsonProperty("owner_inn")
        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        @JsonProperty("producer_inn")
        public String getProducerInn() {
            return producerInn;
        }

        @JsonProperty("producer_inn")
        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        @JsonProperty("production_date")
        public String getProductionDate() {
            return productionDate;
        }

        @JsonProperty("production_date")
        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        @JsonProperty("tnved_code")
        public String getTnvedCode() {
            return tnvedCode;
        }

        @JsonProperty("tnved_code")
        public void setTnvedCode(String tnvedCode) {
            this.tnvedCode = tnvedCode;
        }

        @JsonProperty("uit_code")
        public String getUitCode() {
            return uitCode;
        }

        @JsonProperty("uit_code")
        public void setUitCode(String uitCode) {
            this.uitCode = uitCode;
        }

        @JsonProperty("uitu_code")
        public String getUituCode() {
            return uituCode;
        }

        @JsonProperty("uitu_code")
        public void setUituCode(String uituCode) {
            this.uituCode = uituCode;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "description",
            "doc_id",
            "doc_status",
            "doc_type",
            "importRequest",
            "owner_inn",
            "participant_inn",
            "producer_inn",
            "production_date",
            "production_type",
            "products",
            "reg_date",
            "reg_number"
    })
    @Generated("jsonschema2pojo")
    static class Document {

        @JsonProperty("description")
        private Description description;
        @JsonProperty("doc_id")
        private String docId;
        @JsonProperty("doc_status")
        private String docStatus;
        @JsonProperty("doc_type")
        private String docType;
        @JsonProperty("importRequest")
        private Boolean importRequest;
        @JsonProperty("owner_inn")
        private String ownerInn;
        @JsonProperty("participant_inn")
        private String participantInn;
        @JsonProperty("producer_inn")
        private String producerInn;
        @JsonProperty("production_date")
        private String productionDate;
        @JsonProperty("production_type")
        private String productionType;
        @JsonProperty("products")
        private List<Product> products;
        @JsonProperty("reg_date")
        private String regDate;
        @JsonProperty("reg_number")
        private String regNumber;

        @JsonProperty("description")
        public Description getDescription() {
            return description;
        }

        @JsonProperty("description")
        public void setDescription(Description description) {
            this.description = description;
        }

        @JsonProperty("doc_id")
        public String getDocId() {
            return docId;
        }

        @JsonProperty("doc_id")
        public void setDocId(String docId) {
            this.docId = docId;
        }

        @JsonProperty("doc_status")
        public String getDocStatus() {
            return docStatus;
        }

        @JsonProperty("doc_status")
        public void setDocStatus(String docStatus) {
            this.docStatus = docStatus;
        }

        @JsonProperty("doc_type")
        public String getDocType() {
            return docType;
        }

        @JsonProperty("doc_type")
        public void setDocType(String docType) {
            this.docType = docType;
        }

        @JsonProperty("importRequest")
        public Boolean getImportRequest() {
            return importRequest;
        }

        @JsonProperty("importRequest")
        public void setImportRequest(Boolean importRequest) {
            this.importRequest = importRequest;
        }

        @JsonProperty("owner_inn")
        public String getOwnerInn() {
            return ownerInn;
        }

        @JsonProperty("owner_inn")
        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        @JsonProperty("participant_inn")
        public String getParticipantInn() {
            return participantInn;
        }

        @JsonProperty("participant_inn")
        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }

        @JsonProperty("producer_inn")
        public String getProducerInn() {
            return producerInn;
        }

        @JsonProperty("producer_inn")
        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        @JsonProperty("production_date")
        public String getProductionDate() {
            return productionDate;
        }

        @JsonProperty("production_date")
        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        @JsonProperty("production_type")
        public String getProductionType() {
            return productionType;
        }

        @JsonProperty("production_type")
        public void setProductionType(String productionType) {
            this.productionType = productionType;
        }

        @JsonProperty("products")
        public List<Product> getProducts() {
            return products;
        }

        @JsonProperty("products")
        public void setProducts(List<Product> products) {
            this.products = products;
        }

        @JsonProperty("reg_date")
        public String getRegDate() {
            return regDate;
        }

        @JsonProperty("reg_date")
        public void setRegDate(String regDate) {
            this.regDate = regDate;
        }

        @JsonProperty("reg_number")
        public String getRegNumber() {
            return regNumber;
        }

        @JsonProperty("reg_number")
        public void setRegNumber(String regNumber) {
            this.regNumber = regNumber;
        }
    }

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {

        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 3);
        ObjectMapper objectMapper = new ObjectMapper();

        Object document = objectMapper.readValue(new File(FILE_PATH), Document.class);

        crptApi.createDocument(document, "Signature");

        //checkRateLimiter();
    }


    private static void checkRateLimiter() throws IOException, ParseException, InterruptedException {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 3);
        ObjectMapper objectMapper = new ObjectMapper();

        Object document = objectMapper.readValue(new File(FILE_PATH), Document.class);

        crptApi.createDocument(document, "Signature");

        for (int i = 0; i <= 20; i++) {
            crptApi.createDocument(document, "Signature" + i);
        }
    }
}
