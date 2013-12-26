package ru.spbau.mit.dbmsau.index.btree;

import ru.spbau.mit.dbmsau.pages.Page;

public class GuideNode extends Node {
    public GuideNode(int nodeId, NodeData nodeData, BTree bTree) {
        super(nodeId, nodeData, bTree);
    }

    public static GuideNode getNewGuideNode(Page page, int keySize, int valSize, BTree bTree) {
        NodeData nodeData = NodeData.getNewData(false, page, keySize, valSize);
        return new GuideNode(page.getId(), nodeData, bTree);
    }

    /**
     * Maps the specified key to the specified value in this Node.
     *
     * @return A new right node if this node was split, else null.
     */
    public Node put(TreeTuple key, TreeTuple value) {
        Node newGuide = null;

        int guideIndex = findGuideIndex(key);

        // Recurse to child.
        int childId = this.nodeData.getValue(guideIndex).getInteger(0);
        Node child = this.bTree.getNodeById(childId, true);
        Node newNode = child.put(key, value);
        bTree.releaseNode(childId);

        // Did we split?
        if (newNode != null) {
            // Insert the new key and node at the found index.
            this.nodeData.addKey(guideIndex + 1, newNode.nodeData.getKey(0));
            this.nodeData.addValue(guideIndex + 1, bTree.getNewNodeIdTuple(newNode.nodeId));

            // Do we need to split?
            if (nodeData.getAmountOfKeys() > nodeData.getOrder()) {
                newGuide = bTree.getNewNode(false);

                //newGuide.nodeData.removeKey(0);

                for (int i = nodeData.getAmountOfKeys() / 2; i < nodeData.getAmountOfKeys(); i++) {
                    newGuide.nodeData.addValue(nodeData.getValue(i));
                    newGuide.nodeData.addKey(nodeData.getKey(i));
                }

                nodeData.resize(nodeData.getAmountOfKeys() / 2);

                newGuide.nodeData.setNextNodeId(nodeData.getNextNodeId());
                newGuide.nodeData.setPrevNodeId(this.nodeId);

                if (nodeData.getNextNodeId() != NodeData.NO_NODE_ID) {
                    Node nextNode = bTree.getNodeById(nodeData.getNextNodeId(), true);
                    nextNode.nodeData.setPrevNodeId(newGuide.nodeId);
                    bTree.releaseNode(nextNode);
                }
                nodeData.setNextNodeId(newGuide.nodeId);
            }

            bTree.releaseNode(newNode);
        }

        return newGuide;
    }

//    /**
//     * Removes the specified key from this Node.
//     * @return 0 if nothing was removed, 1 if a key was removed but Nodes did not merge,
//     *   2 if this Node merged with its left sibling, or 3 if this Node merged with its right sibling.
//     */
//    public int remove(Object key)
//    {
//        int guideIndex = findGuideIndex(key);
//
//        // Recurse to child.
//        int result = children.get(guideIndex).remove(key);
//
//        // Was nothing removed?
//        if(result == 0)
//        {
//            return 0;
//        }
//
//        // Was a key removed but no nodes were merged?
//        else if(result == 1)
//        {
//            // It's possible that a key was moved from the left node
//            // or that the index 0 key was removed, and so we need to update
//            // the key for the main node.
//            keys.remove(guideIndex);
//            keys.add(guideIndex, children.get(guideIndex).keys.get(0));
//
//            // It's possible that a key was moved from the right node, and so we need to update the key for that node.
//            if(guideIndex + 1 < keys.size())
//            {
//                keys.remove(guideIndex + 1);
//                keys.add(guideIndex + 1, children.get(guideIndex + 1).keys.get(0));
//            }
//        }
//
//        // Was the child node merged with its left sibling?
//        else if(result == 2)
//        {
//            children.remove(guideIndex);
//            keys.remove(guideIndex);
//        }
//
//        // Was the child node merged with its right sibling?
//        else if(result == 3)
//        {
//            children.remove(guideIndex + 1);
//            keys.remove(guideIndex + 1);
//        }
//
//        // Are we still be above the minimum size?
//        if(keys.size() >= (order + 1) / 2)
//        {
//            return 1;
//        }
//
//        // Otherwise, we need to check the neighbors.
//        else
//        {
//            // Does the left node have more keys than it needs?
//            if(prev != null && prev.keys.size() - 1 >= (order + 1) / 2)
//            {
//                // Simply move the last key from the previous node.
//                int prevIndex = prev.keys.size() - 1;
//                K k = prev.keys.get(prevIndex);
//                Node c = prev.children.get(prevIndex);
//                prev.keys.remove(prevIndex);
//                prev.children.remove(prevIndex);
//                keys.add(0, k);
//                children.add(0, c);
//
//                return 1;
//            }
//
//            // Does the right node have more keys than it needs?
//            else if(next != null && next.keys.size() - 1 >= (order + 1) / 2)
//            {
//                // Simply move the first key from the next node.
//                K k = next.keys.get(0);
//                Node c = next.children.get(0);
//                next.keys.remove(0);
//                next.children.remove(0);
//                keys.add(k);
//                children.add(c);
//
//                return 1;
//            }
//
//            // Otherwise, merge with left?
//            else if(prev != null)
//            {
//                // We actually want to keep the left node, so add all of the keys in this node to the left node.
//                prev.keys.addAll(keys);
//                prev.children.addAll(children);
//                prev.next = next;
//                if(next != null)
//                    next.prev = prev;
//
//                return 2;
//            }
//
//            // Otherwise, merge with right?
//            else if(next != null)
//            {
//                // Add all keys in right node to this node.
//                keys.addAll(next.keys);
//                children.addAll(next.children);
//                if(next.next != null)
//                    next.next.prev = this;
//                next = next.next;
//
//                return 3;
//            }
//
//            // Otherwise, we're the root and it's okay to be less than leafOrder / 2.
//            else
//            {
//                return 1;
//            }
//        }
//    }

    /**
     * Returns the guide index of to use when looking for the specified key.
     */
    private int findGuideIndex(TreeTuple key) {
        return this.bTree.findGuideIndex(this, key);
    }
}
