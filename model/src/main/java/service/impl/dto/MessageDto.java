package service.impl.dto;

public class MessageDto {

    private String text;
    private String authorName;

    public MessageDto(String text, String authorName) {
        this.text = text;
        this.authorName = authorName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDto that = (MessageDto) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return authorName != null ? authorName.equals(that.authorName) : that.authorName == null;

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (authorName != null ? authorName.hashCode() : 0);
        return result;
    }
}
