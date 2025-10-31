//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.List;

public class Document {
    private String title;
    private String content;
    private Category category;
    private Topic topic;
    private List<Tag> tags;

    public Document(String title, String content, Category category, Topic topic, List<Tag> tags) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.topic = topic;
        this.tags = tags;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public Category getCategory() {
        return this.category;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    public String toString() {
        String var10000 = this.title;
        return var10000 + " [" + this.category.getName() + ", " + this.topic.getName() + "]";
    }
}

