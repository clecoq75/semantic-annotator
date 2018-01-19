package cle.nlp.partofspeech;

import cle.nlp.morphology.MorphologicalProperties;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NodeTest {
    @Test(expected = IllegalArgumentException.class)
    public void gather_without_children() {
        Node node = new Node("text", "SENT", null);
        node.gatherChildren("BAM", 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void gather_begin_greater_than_end() {
        createNode().gatherChildren("BAM", 3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void gather_begin_greater_than_size() {
        createNode().gatherChildren("BAM", 10, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void gather_end_greater_than_size() {
        createNode().gatherChildren("BAM", 2, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void gather_begin_less_than_zero() {
        createNode().gatherChildren("BAM", -1, 4);
    }

    @Test
    public void gather() {
        Node node = createNode();
        node.gatherChildren("BAM", 1, 4);
        assertEquals("DT", node.getChildren().get(0).getPartOfSpeech());
        assertEquals("BAM", node.getChildren().get(1).getPartOfSpeech());
        assertEquals("AD", node.getChildren().get(2).getPartOfSpeech());
        assertEquals(3, node.getChildren().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_with_null_text() {
        new Node(null, "KI", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_constructor_with_null_pos() {
        new Node("test", null, null);
    }

    @Test
    public void test_ToString() {
        assertEquals("pok:KI", new Node("pok", "KI", null).toString());
        assertEquals("pok:KI;gender=g;lemma=lemma;nb=p;pers=1", new Node("pok", "KI", new MorphologicalProperties("lemma", "1", "g", "p", null, null)).toString());
    }

    private Node createNode() {
        Node node = new Node("text", "SENT", null);
        node.addChild(new Node("the", "DT", null));
        node.addChild(new Node("small", "AD", null));
        node.addChild(new Node("dog", "NN", null));
        node.addChild(new Node("is", "V", null));
        node.addChild(new Node("green", "AD", null));
        return node;
    }
}
