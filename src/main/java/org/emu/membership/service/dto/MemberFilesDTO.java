package org.emu.membership.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link org.emu.membership.domain.MemberFiles} entity.
 */
public class MemberFilesDTO implements Serializable {

    private Long id;

    @NotNull
    private String fileName;

    @NotNull
    private String fileType;

    @Lob
    private byte[] fileContent;

    private String fileContentContentType;
    private MemberDTO member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContentContentType() {
        return fileContentContentType;
    }

    public void setFileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberFilesDTO)) {
            return false;
        }

        MemberFilesDTO memberFilesDTO = (MemberFilesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memberFilesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberFilesDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", fileContent='" + getFileContent() + "'" +
            ", member=" + getMember() +
            "}";
    }
}
