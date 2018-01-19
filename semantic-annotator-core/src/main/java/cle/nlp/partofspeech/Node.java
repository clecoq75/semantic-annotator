package cle.nlp.partofspeech;

import cle.nlp.morphology.MorphologicalProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
    private List<Node> children;

    private String text;
    private String lowerCaseText;
    private String partOfSpeech;
    private MorphologicalProperties morphologicalProperties;
    private boolean isGatheringNode = false;
    private String representation;

    private Set<String> appliedTaggers;

    private Node(Node node) {
        this.text = node.text;
        this.partOfSpeech = node.partOfSpeech;
        if (node.getChildren()!=null) {
            this.children = new ArrayList<>();
            for (Node child : node.getChildren()) {
                this.children.add(child.copy());
            }
        }
        if (node.morphologicalProperties!=null) {
            this.morphologicalProperties = node.morphologicalProperties.copy();
        }
    }

    public Node(String text, String partOfSpeech, MorphologicalProperties morphologicalProperties) {
        if (text==null) {
            throw new IllegalArgumentException("Text can't be null");
        }
        if (partOfSpeech==null) {
            throw new IllegalArgumentException("Part of speech can't be null");
        }
        this.text = text;
        this.partOfSpeech = partOfSpeech;
        this.morphologicalProperties = morphologicalProperties;
    }

    public String getText() {
        return text;
    }

    public String getLowerCaseText() {
        if (lowerCaseText==null && text!=null) {
            lowerCaseText = text.toLowerCase();
        }
        return lowerCaseText;
    }

    public String getRepresentation() {
        if (representation==null) {
            if (morphologicalProperties!=null && morphologicalProperties.getLemma()!=null) {
                representation = morphologicalProperties.getLemma();
            }
            else {
                representation = getLowerCaseText();
            }
        }
        return representation;
    }

    private void setText(String text) {
        this.text = text;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public MorphologicalProperties getMorphologicalProperties() {
        return morphologicalProperties;
    }

    public void addChild(Node child) {
        if (child!=null) {
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(child);
        }
    }

    public List<Node> getChildren() {
        return children;
    }

    public boolean isGatheringNode() {
        return isGatheringNode;
    }

    private void setGatheringNode() {
        isGatheringNode = true;
    }

    public void gatherChildren(String pos, int beginIndex, int endIndex) {
        checkGatherChildrenIndexes(beginIndex, endIndex);
        if (endIndex!=beginIndex+1 || !children.get(beginIndex).isGatheringNode() || !children.get(beginIndex).getPartOfSpeech().equals(pos)) {
            applyGatherChildren(pos, beginIndex, endIndex);
        }
    }

    private void applyGatherChildren(String pos, int beginIndex, int endIndex) {
        StringBuilder newText = new StringBuilder();
        Node newNode = new Node("", pos, null);
        newNode.setGatheringNode();
        List<Node> newChildren = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            if (i < beginIndex || i >= endIndex) {
                newChildren.add(children.get(i));
            } else {
                if (i == beginIndex) {
                    newChildren.add(newNode);
                }
                if (newText.length() > 0 && newText.charAt(newText.length() - 1) != '\'') {
                    newText.append(" ");
                }
                newText.append(children.get(i).getText());
                newNode.addChild(children.get(i));
            }
        }
        newNode.setText(newText.toString());
        children = newChildren;
    }

    private void checkGatherChildrenIndexes(int beginIndex, int endIndex) {
        if (children==null) {
            throw new IllegalArgumentException("Node has no children.");
        }
        else if (beginIndex<0 || beginIndex>=children.size()) {
            throw new IllegalArgumentException("Invalid begin index : "+beginIndex);
        }
        else if (endIndex<=beginIndex || beginIndex>=children.size()) {
            throw new IllegalArgumentException("Invalid end index : "+endIndex);
        }
        else if (endIndex>children.size()) {
            throw new IllegalArgumentException("Invalid end index : "+endIndex);
        }
    }

    public Node copy() {
        return new Node(this);
    }

    public void addAppliedTagger(String name) {
        if (appliedTaggers==null) {
            appliedTaggers = new HashSet<>();
        }
        appliedTaggers.add(name);
    }

    public boolean taggerHasBeenApplied(String name) {
        return appliedTaggers!=null && appliedTaggers.contains(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getText()).append(':').append(partOfSpeech);
        if (getMorphologicalProperties()!=null) {
            String ts = getMorphologicalProperties().toString();
            if (ts.length()>0) {
                sb.append(';').append(ts);
            }
        }
        return sb.toString();
    }
}
