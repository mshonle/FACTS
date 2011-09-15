package facts.ast;

import java.util.ArrayList;
import java.util.List;

import edu.utsa.strings.Indenter;

public class TreeNode
{
    public TreeNode parent;
    public String label;
    public int postOrderID;
    public List<TreeNode> children = new ArrayList<TreeNode>();
    private int treeSize = -1 /*our memoized size*/;

    public TreeNode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void addChildren(List<TreeNode> newChildren) {
        this.children.addAll(newChildren);
        for (TreeNode child : newChildren) {
            child.parent = this;
        }
    }

    public String prettyPrint(NodeLabeler labeler) {
        StringBuilder buff = new StringBuilder();
        prettyPrint(labeler, this, buff, new Indenter("   "));
        return buff.toString();
    }

    private static void prettyPrint(NodeLabeler labeler, TreeNode treeNode, StringBuilder buff,
            Indenter indenter) {
        buff.append('(');
        buff.append(treeNode.label);
        if (!treeNode.children.isEmpty()) {
            buff.append(':');
            indenter.indent();
            buff.append(indenter.newLine());
            for (TreeNode child : treeNode.children) {
                Integer parsedLabel = Integer.valueOf(child.label);
                if (labeler.hasStringRep(parsedLabel)) {
                    buff.append(child.label);
                    buff.append(':');
                    buff.append(labeler.getStringRep(parsedLabel));
                    buff.append(indenter.newLine());
                }
                else {
                    prettyPrint(labeler, child, buff, indenter);
                }
            }
            indenter.unindent();
        }
        buff.append(')');
        buff.append(indenter.newLine());
    }

    public TreeNode findNode(int postOrderID) {
        if (this.postOrderID == postOrderID) {
            return this;
        }
        else {
            for (TreeNode child : children) {
                TreeNode node = child.findNode(postOrderID);
                if (node != null) {
                    return node;
                }
            }
        }
        return null;
    }

    public TreeNode findNode(String label) {
        if (this.label.equals(label)) {
            return this;
        }
        else {
            for (TreeNode child : children) {
                TreeNode node = child.findNode(label);
                if (node != null) {
                    return node;
                }
            }
        }
        return null;
    }
    
    public int CountNodes(String label) {
        TreeNode foundNode = findNode(label);
        if (foundNode == null) {
            return 0;
        }
        else {
            return foundNode.getSize();
        }
    }
    
    public int getSize() {
        if (treeSize == -1) {
            this.treeSize = 1;
            for (TreeNode child : children) {
                this.treeSize += child.getSize();
            }
        }
        return treeSize;
    }

    public int GetPostOrderID() {
        return postOrderID;
    }

    public int GetChildCount() {
        return children.size();
    }

    public TreeNode GetIthChild(int i) {
        return children.get(i);
    }
}
