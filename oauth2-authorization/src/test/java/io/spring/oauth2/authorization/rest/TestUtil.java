package io.spring.oauth2.authorization.rest;

import org.junit.Ignore;

/**
 * Utility class for testing REST controllers.
 */
@Ignore
public class TestUtil {

//    /** MediaType for JSON UTF8 */
//    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
//            MediaType.APPLICATION_JSON.getType(),
//            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
//
//    /**
//     * Convert an object to JSON byte array.
//     *
//     * @param object
//     *            the object to convert
//     * @return the JSON byte array
//     * @throws IOException
//     */
//    public static byte[] convertObjectToJsonBytes(Object object)
//            throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//        JavaTimeModule module = new JavaTimeModule();
//        module.addSerializer(OffsetDateTime.class, JSR310DateTimeSerializer.INSTANCE);
//        module.addSerializer(ZonedDateTime.class, JSR310DateTimeSerializer.INSTANCE);
//        module.addSerializer(LocalDateTime.class, JSR310DateTimeSerializer.INSTANCE);
//        module.addSerializer(Instant.class, JSR310DateTimeSerializer.INSTANCE);
//        module.addDeserializer(LocalDate.class, JSR310LocalDateDeserializer.INSTANCE);
//        mapper.registerModule(module);
//
//        return mapper.writeValueAsBytes(object);
//    }
//
//    /**
//     * Create a byte array with a specific size filled with specified data.
//     *
//     * @param size the size of the byte array
//     * @param data the data to put in the byte array
//     */
//    public static byte[] createByteArray(int size, String data) {
//        byte[] byteArray = new byte[size];
//        for (int i = 0; i < size; i++) {
//            byteArray[i] = Byte.parseByte(data, 2);
//        }
//        return byteArray;
//    }
}
