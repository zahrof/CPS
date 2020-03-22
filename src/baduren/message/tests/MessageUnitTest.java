package baduren.message.tests;


import static org.junit.jupiter.api.Assertions.*;

import baduren.interfaces.MessageI;
import baduren.message.Message;
import baduren.interfaces.MessageFilterI;
import baduren.message.Properties;
import baduren.message.TimeStamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The class to make unit tests for the messages
 */
class MessageUnitTest {

    private MessageI message1;
    private MessageI message2;
    private MessageI message3;

    /**
     * Initialization of the messages to test
     *
     * @throws InterruptedException the interrupted exception
     */
    @BeforeEach
    public void init() throws InterruptedException {
        try {


            this.message1 = new Message("Le tout premier message", new TimeStamp(System.currentTimeMillis(), "aaa"), new Properties());
            Thread.sleep(1);
            this.message2 = new Message("Ceci est le deuxième message", new TimeStamp(System.currentTimeMillis(), "bbb"), new Properties());
            Thread.sleep(1);
            this.message3 = new Message("L'année est terminée, tout le monde a son master", new TimeStamp(System.currentTimeMillis(), "ccc"), new Properties());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // === === === Test sequence === === === //


    /**
     * Test filter.
     */
    @Test
    void testFilter() {
        message1.getProperties().putProp("Nombre", 2);
        message1.getProperties().putProp("Lettre", 'a');
        message1.getProperties().putProp("Niveau", "important");
        message2.getProperties().putProp("Modifie", true);
        message2.getProperties().putProp("Likes", (byte) 116);
        message2.getProperties().putProp("Coef", 0.7);
        message2.getProperties().putProp("Temperature", 1.390f);
        message2.getProperties().putProp("Littres", 1000L);
        message2.getProperties().putProp("AgeMerlin", (short) 9843);


        MessageFilterI messagefilter1 = new MessageFilterI() {
            @Override
            public boolean filter(MessageI m) throws Exception {
                Character c = m.getProperties().getCharProp("Lettre");
                Integer i = m.getProperties().getIntProp("Nombre");
                String s = m.getProperties().getStringProp("Niveau");
                return (s != null && i != null && c != null) &&
                        c == 'a' && i < 15 && s.equals("important");
            }

            @Override
            public String getName() {
                return "messagefilter1";
            }
        };

        MessageFilterI messagefilter2 = new MessageFilterI() {
            @Override
            public boolean filter(MessageI m) throws Exception {
                Long l = m.getProperties().getLongProp("Littres");
                return l != null && l > 400L;
            }

            @Override
            public String getName() {
                return "messagefilter2";
            }
        };

        try {
            assertTrue(messagefilter1.filter(message1));
            assertFalse(messagefilter1.filter(message2));
            assertTrue(messagefilter2.filter(message2));
            assertFalse(messagefilter2.filter(message1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }








    /**
     * Test message uri.
     */
    @Test
    void testMessageURI() {
        assertNotEquals(message1.getURI(), message2.getURI());
    }

    /**
     * Test timestamp.
     */
    @Test
    void testTimestamp() {
        assertEquals(message1.getTimeStamp().getTimeStamper(), "aaa");
        assertEquals(message2.getTimeStamp().getTimeStamper(), "bbb");
        assertTrue(message1.getTimeStamp().getTime() < message2.getTimeStamp().getTime());
    }

    /**
     * Test content.
     */
    @Test
    void testContent() {
        assertEquals(message1.getPayload().toString(), "Le tout premier message");
        assertEquals(message2.getPayload().toString(), "Ceci est le deuxième message");
        assertEquals(message3.getPayload().toString(), "L'année est terminée, tout le monde a son master");
    }






    /**
     * First properties test
     */
    @Test
    void firstTestProperties() {
        message1.getProperties().putProp("ouiiii", (double) 2.332); //double
        assertEquals(0, message1.getProperties().getIntProp("ouiiii"));
        assertEquals(2.332, message1.getProperties().getDoubleProp("ouiiii"));
    }

    /**
     * Second properties test
     */
    @Test
    void secondTestProperties() {
        message1.getProperties().putProp("likeFB", 3372);
        message1.getProperties().putProp("likeTwitter", 736);
        message1.getProperties().putProp("likeYouTube", 8277362);

        assertNotEquals(message1.getProperties().getIntProp("likeFB"),
                message1.getProperties().getIntProp("likeTwitter"));
        assertNotEquals(message1.getProperties().getIntProp("likeTwitter"),
                message1.getProperties().getIntProp("likeYouTube"));
        assertEquals(message1.getProperties().getIntProp("likeTwitter"),
                736);
    }

    /**
     * Third properties test
     */
    @Test
    void thirdTestProperties() {

        message1.getProperties().putProp("_", true); //boolean
        message1.getProperties().putProp("_", (byte) 1); //byte
        message1.getProperties().putProp("_", '1'); //char
        message1.getProperties().putProp("_", 1.0); //dobule
        message1.getProperties().putProp("_", 1.0f); //float
        message1.getProperties().putProp("_", 1); //int
        message1.getProperties().putProp("_", 1L); //long
        message1.getProperties().putProp("_", (short) 1); //short
        message1.getProperties().putProp("_", "1"); //String

        assertNotEquals(false, message1.getProperties().getBooleanProp("_"));
        assertNotEquals(0, message1.getProperties().getByteProp("_"));
        assertNotEquals(' ', message1.getProperties().getCharProp("_"));
        assertNotEquals(0, message1.getProperties().getDoubleProp("_"));
        assertNotEquals(0, message1.getProperties().getFloatProp("_"));
        assertNotEquals(0, message1.getProperties().getIntProp("_"));
        assertNotEquals(0, message1.getProperties().getLongProp("_"));
        assertNotEquals(0, message1.getProperties().getShortProp("_"));
        assertNotEquals("", message1.getProperties().getStringProp("_"));
    }




}