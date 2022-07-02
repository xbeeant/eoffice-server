package io.github.xbeeant.eoffice.service.render.office;

import com.alibaba.fastjson.annotation.JSONField;
import io.github.xbeeant.spring.security.SecurityUser;

import java.util.List;

/**
 * @author xiaobiao
 * @version 2021/7/4
 */
@SuppressWarnings("unused")
public class OfficeConfig {

    private String documentType;

    private String height = "100%";

    private String token;

    private String type = "desktop";

    private String width = "100%";

    private Document document;

    private EditorConfig editorConfig;

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = DocumentType.getDocumentType(documentType);
    }

    public EditorConfig getEditorConfig() {
        return editorConfig;
    }

    public void setEditorConfig(EditorConfig editorConfig) {
        this.editorConfig = editorConfig;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public static class Document {
        private String fileType;

        private String key;

        private String title;

        private String url;

        private Info info;
        private Permissions permissions;


        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public Info getInfo() {
            return info;
        }

        public void setInfo(Info info) {
            this.info = info;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Permissions getPermissions() {
            return permissions;
        }

        public void setPermissions(Permissions permissions) {
            this.permissions = permissions;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public static class Info {
            private Boolean favorite;
            private String folder;
            private String owner;
            private List<SharingSettings> sharingSettings;
            private String uploaded;

            public Boolean getFavorite() {
                return favorite;
            }

            public void setFavorite(Boolean favorite) {
                this.favorite = favorite;
            }

            public String getFolder() {
                return folder;
            }

            public void setFolder(String folder) {
                this.folder = folder;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public List<SharingSettings> getSharingSettings() {
                return sharingSettings;
            }

            public void setSharingSettings(List<SharingSettings> sharingSettings) {
                this.sharingSettings = sharingSettings;
            }

            public String getUploaded() {
                return uploaded;
            }

            public void setUploaded(String uploaded) {
                this.uploaded = uploaded;
            }

            public static class SharingSettings {
                private Boolean isLink;
                /**
                 * the access rights for the user with the name above. Can be
                 * <p>
                 * Full Access
                 * Read Only
                 * Deny Access
                 */
                private String permissions;
                private String user;

                public Boolean getIsLink() {
                    return isLink;
                }

                public void setIsLink(Boolean isLink) {
                    this.isLink = isLink;
                }

                public String getPermissions() {
                    return permissions;
                }

                public void setPermissions(String permissions) {
                    this.permissions = permissions;
                }

                public String getUser() {
                    return user;
                }

                public void setUser(String user) {
                    this.user = user;
                }
            }
        }

        public static class Permissions {
            private Boolean chat = true;
            private Boolean comment = false;
            private List<CommentGroup> commentGroups;
            private Boolean copy = false;
            private Boolean deleteCommentAuthorOnly = false;
            private Boolean download = false;
            private Boolean edit = false;
            private Boolean editCommentAuthorOnly = false;
            private Boolean fillForms = false;
            private Boolean modifyContentControl;
            private Boolean modifyFilter;
            private Boolean print = false;

            private Boolean protect;

            private Boolean rename;
            private Boolean review;
            private List<String> reviewGroups;

            private List<String> userInfoGroups;

            public Boolean getComment() {
                return comment;
            }

            public void setComment(Boolean comment) {
                this.comment = comment;
            }

            public Boolean getCopy() {
                return copy;
            }

            public void setCopy(Boolean copy) {
                this.copy = copy;
            }

            public Boolean getDeleteCommentAuthorOnly() {
                return deleteCommentAuthorOnly;
            }

            public void setDeleteCommentAuthorOnly(Boolean deleteCommentAuthorOnly) {
                this.deleteCommentAuthorOnly = deleteCommentAuthorOnly;
            }

            public Boolean getDownload() {
                return download;
            }

            public void setDownload(Boolean download) {
                this.download = download;
            }

            public Boolean getEdit() {
                return edit;
            }

            public void setEdit(Boolean edit) {
                this.edit = edit;
            }

            public Boolean getEditCommentAuthorOnly() {
                return editCommentAuthorOnly;
            }

            public void setEditCommentAuthorOnly(Boolean editCommentAuthorOnly) {
                this.editCommentAuthorOnly = editCommentAuthorOnly;
            }

            public Boolean getFillForms() {
                return fillForms;
            }

            public void setFillForms(Boolean fillForms) {
                this.fillForms = fillForms;
            }

            public Boolean getModifyContentControl() {
                return modifyContentControl;
            }

            public void setModifyContentControl(Boolean modifyContentControl) {
                this.modifyContentControl = modifyContentControl;
            }

            public Boolean getModifyFilter() {
                return modifyFilter;
            }

            public void setModifyFilter(Boolean modifyFilter) {
                this.modifyFilter = modifyFilter;
            }

            public Boolean getPrint() {
                return print;
            }

            public void setPrint(Boolean print) {
                this.print = print;
            }

            public Boolean getReview() {
                return review;
            }

            public void setReview(Boolean review) {
                this.review = review;
            }

            public List<String> getReviewGroups() {
                return reviewGroups;
            }

            public void setReviewGroups(List<String> reviewGroups) {
                this.reviewGroups = reviewGroups;
            }

            public Boolean getChat() {
                return chat;
            }

            public void setChat(Boolean chat) {
                this.chat = chat;
            }

            public List<CommentGroup> getCommentGroups() {
                return commentGroups;
            }

            public void setCommentGroups(List<CommentGroup> commentGroups) {
                this.commentGroups = commentGroups;
            }

            public Boolean getProtect() {
                return protect;
            }

            public void setProtect(Boolean protect) {
                this.protect = protect;
            }

            public Boolean getRename() {
                return rename;
            }

            public void setRename(Boolean rename) {
                this.rename = rename;
            }

            public List<String> getUserInfoGroups() {
                return userInfoGroups;
            }

            public void setUserInfoGroups(List<String> userInfoGroups) {
                this.userInfoGroups = userInfoGroups;
            }

            public static class CommentGroup {
                private List<String> edit;

                private List<String> remove;

                private List<String> view;

                public List<String> getEdit() {
                    return edit;
                }

                public void setEdit(List<String> edit) {
                    this.edit = edit;
                }

                public List<String> getRemove() {
                    return remove;
                }

                public void setRemove(List<String> remove) {
                    this.remove = remove;
                }

                public List<String> getView() {
                    return view;
                }

                public void setView(List<String> view) {
                    this.view = view;
                }
            }
        }
    }

    public static class EditorConfig {
        private String actionLink;
        private String callbackUrl;

        private CoEditing coEditing;

        private String createUrl;

        private String lang = "zh-CN";

        private String location = "zh-CN";

        private String mode = "edit";

        private List<Recent> recent;
        private String region;
        private List<Templates> templates;
        private User user;

        private Customization customization = new Customization();
        private Embedded embedded;
        private Plugins plugins;

        public String getActionLink() {
            return actionLink;
        }

        public void setActionLink(String actionLink) {
            this.actionLink = actionLink;
        }

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
        }

        public String getCreateUrl() {
            return createUrl;
        }

        public void setCreateUrl(String createUrl) {
            this.createUrl = createUrl;
        }

        public Customization getCustomization() {
            return customization;
        }

        public void setCustomization(Customization customization) {
            this.customization = customization;
        }

        public Embedded getEmbedded() {
            return embedded;
        }

        public void setEmbedded(Embedded embedded) {
            this.embedded = embedded;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public Plugins getPlugins() {
            return plugins;
        }

        public void setPlugins(Plugins plugins) {
            this.plugins = plugins;
        }

        public List<Recent> getRecent() {
            return recent;
        }

        public void setRecent(List<Recent> recent) {
            this.recent = recent;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public List<Templates> getTemplates() {
            return templates;
        }

        public void setTemplates(List<Templates> templates) {
            this.templates = templates;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public static class CoEditing {
            private String mode = "fast";

            private Boolean change = true;

            public String getMode() {
                return mode;
            }

            public void setMode(String mode) {
                this.mode = mode;
            }

            public Boolean getChange() {
                return change;
            }

            public void setChange(Boolean change) {
                this.change = change;
            }
        }

        public static class Customization {
            private Anonymous anonymous;
            private Boolean autosave = true;
            private Boolean comments = true;
            private Boolean compactHeader = false;
            private Boolean compactToolbar = false;
            private Boolean compatibleFeatures = false;
            private Customer customer;

            private Features features;

            private Feedback feedback;
            private Boolean forcesave = false;
            private Goback goback;
            private Boolean help = true;

            private Boolean hideNotes = false;
            private Boolean hideRightMenu = false;
            private Boolean hideRulers = false;
            private Logo logo;
            private Boolean macros = true;
            private String macrosMode = "warn";
            private Boolean mentionShare = true;
            private Boolean plugins;

            private Review review;
            private Boolean toolbarHideFileName = false;
            private Boolean toolbarNoTabs = false;

            private String uiTheme;
            private String unit = "cm";
            private Integer zoom = 100;

            public Anonymous getAnonymous() {
                return anonymous;
            }

            public void setAnonymous(Anonymous anonymous) {
                this.anonymous = anonymous;
            }

            public Boolean getAutosave() {
                return autosave;
            }

            public void setAutosave(Boolean autosave) {
                this.autosave = autosave;
            }

            public Features getFeatures() {
                return features;
            }

            public void setFeatures(Features features) {
                this.features = features;
            }

            public Boolean getHideNotes() {
                return hideNotes;
            }

            public void setHideNotes(Boolean hideNotes) {
                this.hideNotes = hideNotes;
            }

            public Review getReview() {
                return review;
            }

            public void setReview(Review review) {
                this.review = review;
            }

            public String getUiTheme() {
                return uiTheme;
            }

            public void setUiTheme(String uiTheme) {
                this.uiTheme = uiTheme;
            }

            public Boolean getComments() {
                return comments;
            }

            public void setComments(Boolean comments) {
                this.comments = comments;
            }

            public Boolean getCompactHeader() {
                return compactHeader;
            }

            public void setCompactHeader(Boolean compactHeader) {
                this.compactHeader = compactHeader;
            }

            public Boolean getCompactToolbar() {
                return compactToolbar;
            }

            public void setCompactToolbar(Boolean compactToolbar) {
                this.compactToolbar = compactToolbar;
            }

            public Boolean getCompatibleFeatures() {
                return compatibleFeatures;
            }

            public void setCompatibleFeatures(Boolean compatibleFeatures) {
                this.compatibleFeatures = compatibleFeatures;
            }

            public Customer getCustomer() {
                return customer;
            }

            public void setCustomer(Customer customer) {
                this.customer = customer;
            }

            public Feedback getFeedback() {
                return feedback;
            }

            public void setFeedback(Feedback feedback) {
                this.feedback = feedback;
            }

            public Boolean getForcesave() {
                return forcesave;
            }

            public void setForcesave(Boolean forcesave) {
                this.forcesave = forcesave;
            }

            public Goback getGoback() {
                return goback;
            }

            public void setGoback(Goback goback) {
                this.goback = goback;
            }

            public Boolean getHelp() {
                return help;
            }

            public void setHelp(Boolean help) {
                this.help = help;
            }

            public Boolean getHideRightMenu() {
                return hideRightMenu;
            }

            public void setHideRightMenu(Boolean hideRightMenu) {
                this.hideRightMenu = hideRightMenu;
            }

            public Boolean getHideRulers() {
                return hideRulers;
            }

            public void setHideRulers(Boolean hideRulers) {
                this.hideRulers = hideRulers;
            }

            public Logo getLogo() {
                return logo;
            }

            public void setLogo(Logo logo) {
                this.logo = logo;
            }

            public Boolean getMacros() {
                return macros;
            }

            public void setMacros(Boolean macros) {
                this.macros = macros;
            }

            public String getMacrosMode() {
                return macrosMode;
            }

            public void setMacrosMode(String macrosMode) {
                this.macrosMode = macrosMode;
            }

            public Boolean getMentionShare() {
                return mentionShare;
            }

            public void setMentionShare(Boolean mentionShare) {
                this.mentionShare = mentionShare;
            }

            public Boolean getPlugins() {
                return plugins;
            }

            public void setPlugins(Boolean plugins) {
                this.plugins = plugins;
            }


            public Boolean getToolbarHideFileName() {
                return toolbarHideFileName;
            }

            public void setToolbarHideFileName(Boolean toolbarHideFileName) {
                this.toolbarHideFileName = toolbarHideFileName;
            }

            public Boolean getToolbarNoTabs() {
                return toolbarNoTabs;
            }

            public void setToolbarNoTabs(Boolean toolbarNoTabs) {
                this.toolbarNoTabs = toolbarNoTabs;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public Integer getZoom() {
                return zoom;
            }

            public void setZoom(Integer zoom) {
                this.zoom = zoom;
            }

            public static class Review {
                private Boolean hideReviewDisplay;

                private Boolean hoverMode;

                /**
                 * – markup - the document is displayed with proposed changes highlighted;
                 * – simple - the document is displayed with proposed changes highlighted, but the balloons are turned off;
                 * – final - the document is displayed with all the proposed changes applied;
                 * – original - the original document is displayed without the proposed changes.
                 * The default value is original,
                 */
                private String reviewDisplay = "original";

                private Boolean showReviewChanges;

                private Boolean trackChanges;

                public Boolean getHideReviewDisplay() {
                    return hideReviewDisplay;
                }

                public void setHideReviewDisplay(Boolean hideReviewDisplay) {
                    this.hideReviewDisplay = hideReviewDisplay;
                }

                public Boolean getHoverMode() {
                    return hoverMode;
                }

                public void setHoverMode(Boolean hoverMode) {
                    this.hoverMode = hoverMode;
                }

                public String getReviewDisplay() {
                    return reviewDisplay;
                }

                public void setReviewDisplay(String reviewDisplay) {
                    this.reviewDisplay = reviewDisplay;
                }

                public Boolean getShowReviewChanges() {
                    return showReviewChanges;
                }

                public void setShowReviewChanges(Boolean showReviewChanges) {
                    this.showReviewChanges = showReviewChanges;
                }

                public Boolean getTrackChanges() {
                    return trackChanges;
                }

                public void setTrackChanges(Boolean trackChanges) {
                    this.trackChanges = trackChanges;
                }
            }

            public static class Features {
                private Boolean spellcheck;

                @JSONField(name = "spellcheck.mode")
                private Boolean spellcheckMode;

                public Boolean getSpellcheck() {
                    return spellcheck;
                }

                public void setSpellcheck(Boolean spellcheck) {
                    this.spellcheck = spellcheck;
                }

                public Boolean getSpellcheckMode() {
                    return spellcheckMode;
                }

                public void setSpellcheckMode(Boolean spellcheckMode) {
                    this.spellcheckMode = spellcheckMode;
                }
            }

            public static class Anonymous {
                private String label;
                private Boolean request;

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }

                public Boolean getRequest() {
                    return request;
                }

                public void setRequest(Boolean request) {
                    this.request = request;
                }
            }

            public static class Customer {
                private String address;
                private String info;
                private String logo;

                private String logoDark;
                private String mail;
                private String name;
                private String www;

                public String getLogoDark() {
                    return logoDark;
                }

                public void setLogoDark(String logoDark) {
                    this.logoDark = logoDark;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public String getLogo() {
                    return logo;
                }

                public void setLogo(String logo) {
                    this.logo = logo;
                }

                public String getMail() {
                    return mail;
                }

                public void setMail(String mail) {
                    this.mail = mail;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getWww() {
                    return www;
                }

                public void setWww(String www) {
                    this.www = www;
                }
            }

            public static class Feedback {
                private String url;
                private Boolean visible;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public Boolean getVisible() {
                    return visible;
                }

                public void setVisible(Boolean visible) {
                    this.visible = visible;
                }
            }

            public static class Goback {
                private Boolean blank;
                private Boolean requestClose;
                private String text;
                private String url;

                public Boolean getBlank() {
                    return blank;
                }

                public void setBlank(Boolean blank) {
                    this.blank = blank;
                }

                public Boolean getRequestClose() {
                    return requestClose;
                }

                public void setRequestClose(Boolean requestClose) {
                    this.requestClose = requestClose;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public static class Logo {
                private String image = "";

                private String imageDark = "";
                private String imageEmbedded;
                private String url;

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getImageEmbedded() {
                    return imageEmbedded;
                }

                public void setImageEmbedded(String imageEmbedded) {
                    this.imageEmbedded = imageEmbedded;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getImageDark() {
                    return imageDark;
                }

                public void setImageDark(String imageDark) {
                    this.imageDark = imageDark;
                }
            }
        }

        public static class Embedded {
            private String embedUrl;
            private String fullscreenUrl;
            private String saveUrl;
            private String shareUrl;
            private String toolbarDocked;

            public String getEmbedUrl() {
                return embedUrl;
            }

            public void setEmbedUrl(String embedUrl) {
                this.embedUrl = embedUrl;
            }

            public String getFullscreenUrl() {
                return fullscreenUrl;
            }

            public void setFullscreenUrl(String fullscreenUrl) {
                this.fullscreenUrl = fullscreenUrl;
            }

            public String getSaveUrl() {
                return saveUrl;
            }

            public void setSaveUrl(String saveUrl) {
                this.saveUrl = saveUrl;
            }

            public String getShareUrl() {
                return shareUrl;
            }

            public void setShareUrl(String shareUrl) {
                this.shareUrl = shareUrl;
            }

            public String getToolbarDocked() {
                return toolbarDocked;
            }

            public void setToolbarDocked(String toolbarDocked) {
                this.toolbarDocked = toolbarDocked;
            }
        }

        public static class Plugins {
            private List<String> autostart;
            private List<String> pluginsData;

            public List<String> getAutostart() {
                return autostart;
            }

            public void setAutostart(List<String> autostart) {
                this.autostart = autostart;
            }

            public List<String> getPluginsData() {
                return pluginsData;
            }

            public void setPluginsData(List<String> pluginsData) {
                this.pluginsData = pluginsData;
            }
        }

        public static class User {
            private String group;
            private String id;
            private String name;

            public User() {
            }

            public User(SecurityUser<io.github.xbeeant.eoffice.model.User> currentUser) {
                this.id = currentUser.getUserId();
                this.name = currentUser.getUserNickname();
            }

            public String getGroup() {
                return group;
            }

            public void setGroup(String group) {
                this.group = group;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class Recent {
            private String folder;
            private String title;
            private String url;

            public String getFolder() {
                return folder;
            }

            public void setFolder(String folder) {
                this.folder = folder;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class Templates {
            private String image;
            private String title;
            private String url;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
