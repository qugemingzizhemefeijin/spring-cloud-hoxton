// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: proto/UbixBidRequest.proto
// Protobuf Java Version: 4.28.2

package com.atguigu.springcloud.other.proto;

public final class UbixBidRequestModel {
  private UbixBidRequestModel() {}
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 28,
      /* patch= */ 2,
      /* suffix= */ "",
      UbixBidRequestModel.class.getName());
  }
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface UbixBidRequestOrBuilder extends
      // @@protoc_insertion_point(interface_extends:UbixBidRequest)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>string request_id = 1;</code>
     * @return The requestId.
     */
    java.lang.String getRequestId();
    /**
     * <code>string request_id = 1;</code>
     * @return The bytes for requestId.
     */
    com.google.protobuf.ByteString
        getRequestIdBytes();

    /**
     * <code>string api_version = 2;</code>
     * @return The apiVersion.
     */
    java.lang.String getApiVersion();
    /**
     * <code>string api_version = 2;</code>
     * @return The bytes for apiVersion.
     */
    com.google.protobuf.ByteString
        getApiVersionBytes();

    /**
     * <code>.UbixUser user = 4;</code>
     * @return Whether the user field is set.
     */
    boolean hasUser();
    /**
     * <code>.UbixUser user = 4;</code>
     * @return The user.
     */
    com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser getUser();
    /**
     * <code>.UbixUser user = 4;</code>
     */
    com.atguigu.springcloud.other.proto.UbixUserModel.UbixUserOrBuilder getUserOrBuilder();
  }
  /**
   * Protobuf type {@code UbixBidRequest}
   */
  public static final class UbixBidRequest extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:UbixBidRequest)
      UbixBidRequestOrBuilder {
  private static final long serialVersionUID = 0L;
    static {
      com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
        com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
        /* major= */ 4,
        /* minor= */ 28,
        /* patch= */ 2,
        /* suffix= */ "",
        UbixBidRequest.class.getName());
    }
    // Use UbixBidRequest.newBuilder() to construct.
    private UbixBidRequest(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
    }
    private UbixBidRequest() {
      requestId_ = "";
      apiVersion_ = "";
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.atguigu.springcloud.other.proto.UbixBidRequestModel.internal_static_UbixBidRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.atguigu.springcloud.other.proto.UbixBidRequestModel.internal_static_UbixBidRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest.class, com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest.Builder.class);
    }

    private int bitField0_;
    public static final int REQUEST_ID_FIELD_NUMBER = 1;
    @SuppressWarnings("serial")
    private volatile java.lang.Object requestId_ = "";
    /**
     * <code>string request_id = 1;</code>
     * @return The requestId.
     */
    @java.lang.Override
    public java.lang.String getRequestId() {
      java.lang.Object ref = requestId_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        requestId_ = s;
        return s;
      }
    }
    /**
     * <code>string request_id = 1;</code>
     * @return The bytes for requestId.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getRequestIdBytes() {
      java.lang.Object ref = requestId_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        requestId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int API_VERSION_FIELD_NUMBER = 2;
    @SuppressWarnings("serial")
    private volatile java.lang.Object apiVersion_ = "";
    /**
     * <code>string api_version = 2;</code>
     * @return The apiVersion.
     */
    @java.lang.Override
    public java.lang.String getApiVersion() {
      java.lang.Object ref = apiVersion_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        apiVersion_ = s;
        return s;
      }
    }
    /**
     * <code>string api_version = 2;</code>
     * @return The bytes for apiVersion.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getApiVersionBytes() {
      java.lang.Object ref = apiVersion_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        apiVersion_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int USER_FIELD_NUMBER = 4;
    private com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser user_;
    /**
     * <code>.UbixUser user = 4;</code>
     * @return Whether the user field is set.
     */
    @java.lang.Override
    public boolean hasUser() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>.UbixUser user = 4;</code>
     * @return The user.
     */
    @java.lang.Override
    public com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser getUser() {
      return user_ == null ? com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser.getDefaultInstance() : user_;
    }
    /**
     * <code>.UbixUser user = 4;</code>
     */
    @java.lang.Override
    public com.atguigu.springcloud.other.proto.UbixUserModel.UbixUserOrBuilder getUserOrBuilder() {
      return user_ == null ? com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser.getDefaultInstance() : user_;
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!com.google.protobuf.GeneratedMessage.isStringEmpty(requestId_)) {
        com.google.protobuf.GeneratedMessage.writeString(output, 1, requestId_);
      }
      if (!com.google.protobuf.GeneratedMessage.isStringEmpty(apiVersion_)) {
        com.google.protobuf.GeneratedMessage.writeString(output, 2, apiVersion_);
      }
      if (((bitField0_ & 0x00000001) != 0)) {
        output.writeMessage(4, getUser());
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!com.google.protobuf.GeneratedMessage.isStringEmpty(requestId_)) {
        size += com.google.protobuf.GeneratedMessage.computeStringSize(1, requestId_);
      }
      if (!com.google.protobuf.GeneratedMessage.isStringEmpty(apiVersion_)) {
        size += com.google.protobuf.GeneratedMessage.computeStringSize(2, apiVersion_);
      }
      if (((bitField0_ & 0x00000001) != 0)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, getUser());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest)) {
        return super.equals(obj);
      }
      com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest other = (com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest) obj;

      if (!getRequestId()
          .equals(other.getRequestId())) return false;
      if (!getApiVersion()
          .equals(other.getApiVersion())) return false;
      if (hasUser() != other.hasUser()) return false;
      if (hasUser()) {
        if (!getUser()
            .equals(other.getUser())) return false;
      }
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + REQUEST_ID_FIELD_NUMBER;
      hash = (53 * hash) + getRequestId().hashCode();
      hash = (37 * hash) + API_VERSION_FIELD_NUMBER;
      hash = (53 * hash) + getApiVersion().hashCode();
      if (hasUser()) {
        hash = (37 * hash) + USER_FIELD_NUMBER;
        hash = (53 * hash) + getUser().hashCode();
      }
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input);
    }
    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseDelimitedWithIOException(PARSER, input);
    }

    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input);
    }
    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code UbixBidRequest}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:UbixBidRequest)
        com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.atguigu.springcloud.other.proto.UbixBidRequestModel.internal_static_UbixBidRequest_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.atguigu.springcloud.other.proto.UbixBidRequestModel.internal_static_UbixBidRequest_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest.class, com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest.Builder.class);
      }

      // Construct using com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage
                .alwaysUseFieldBuilders) {
          getUserFieldBuilder();
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        requestId_ = "";
        apiVersion_ = "";
        user_ = null;
        if (userBuilder_ != null) {
          userBuilder_.dispose();
          userBuilder_ = null;
        }
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.atguigu.springcloud.other.proto.UbixBidRequestModel.internal_static_UbixBidRequest_descriptor;
      }

      @java.lang.Override
      public com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest getDefaultInstanceForType() {
        return com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest.getDefaultInstance();
      }

      @java.lang.Override
      public com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest build() {
        com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest buildPartial() {
        com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest result = new com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest(this);
        if (bitField0_ != 0) { buildPartial0(result); }
        onBuilt();
        return result;
      }

      private void buildPartial0(com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.requestId_ = requestId_;
        }
        if (((from_bitField0_ & 0x00000002) != 0)) {
          result.apiVersion_ = apiVersion_;
        }
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000004) != 0)) {
          result.user_ = userBuilder_ == null
              ? user_
              : userBuilder_.build();
          to_bitField0_ |= 0x00000001;
        }
        result.bitField0_ |= to_bitField0_;
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest) {
          return mergeFrom((com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest other) {
        if (other == com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest.getDefaultInstance()) return this;
        if (!other.getRequestId().isEmpty()) {
          requestId_ = other.requestId_;
          bitField0_ |= 0x00000001;
          onChanged();
        }
        if (!other.getApiVersion().isEmpty()) {
          apiVersion_ = other.apiVersion_;
          bitField0_ |= 0x00000002;
          onChanged();
        }
        if (other.hasUser()) {
          mergeUser(other.getUser());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10: {
                requestId_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000001;
                break;
              } // case 10
              case 18: {
                apiVersion_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000002;
                break;
              } // case 18
              case 34: {
                input.readMessage(
                    getUserFieldBuilder().getBuilder(),
                    extensionRegistry);
                bitField0_ |= 0x00000004;
                break;
              } // case 34
              default: {
                if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                  done = true; // was an endgroup tag
                }
                break;
              } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }
      private int bitField0_;

      private java.lang.Object requestId_ = "";
      /**
       * <code>string request_id = 1;</code>
       * @return The requestId.
       */
      public java.lang.String getRequestId() {
        java.lang.Object ref = requestId_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          requestId_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string request_id = 1;</code>
       * @return The bytes for requestId.
       */
      public com.google.protobuf.ByteString
          getRequestIdBytes() {
        java.lang.Object ref = requestId_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          requestId_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string request_id = 1;</code>
       * @param value The requestId to set.
       * @return This builder for chaining.
       */
      public Builder setRequestId(
          java.lang.String value) {
        if (value == null) { throw new NullPointerException(); }
        requestId_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>string request_id = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearRequestId() {
        requestId_ = getDefaultInstance().getRequestId();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       * <code>string request_id = 1;</code>
       * @param value The bytes for requestId to set.
       * @return This builder for chaining.
       */
      public Builder setRequestIdBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        checkByteStringIsUtf8(value);
        requestId_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }

      private java.lang.Object apiVersion_ = "";
      /**
       * <code>string api_version = 2;</code>
       * @return The apiVersion.
       */
      public java.lang.String getApiVersion() {
        java.lang.Object ref = apiVersion_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          apiVersion_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string api_version = 2;</code>
       * @return The bytes for apiVersion.
       */
      public com.google.protobuf.ByteString
          getApiVersionBytes() {
        java.lang.Object ref = apiVersion_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          apiVersion_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string api_version = 2;</code>
       * @param value The apiVersion to set.
       * @return This builder for chaining.
       */
      public Builder setApiVersion(
          java.lang.String value) {
        if (value == null) { throw new NullPointerException(); }
        apiVersion_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      /**
       * <code>string api_version = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearApiVersion() {
        apiVersion_ = getDefaultInstance().getApiVersion();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      /**
       * <code>string api_version = 2;</code>
       * @param value The bytes for apiVersion to set.
       * @return This builder for chaining.
       */
      public Builder setApiVersionBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        checkByteStringIsUtf8(value);
        apiVersion_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }

      private com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser user_;
      private com.google.protobuf.SingleFieldBuilder<
          com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser, com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser.Builder, com.atguigu.springcloud.other.proto.UbixUserModel.UbixUserOrBuilder> userBuilder_;
      /**
       * <code>.UbixUser user = 4;</code>
       * @return Whether the user field is set.
       */
      public boolean hasUser() {
        return ((bitField0_ & 0x00000004) != 0);
      }
      /**
       * <code>.UbixUser user = 4;</code>
       * @return The user.
       */
      public com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser getUser() {
        if (userBuilder_ == null) {
          return user_ == null ? com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser.getDefaultInstance() : user_;
        } else {
          return userBuilder_.getMessage();
        }
      }
      /**
       * <code>.UbixUser user = 4;</code>
       */
      public Builder setUser(com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser value) {
        if (userBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          user_ = value;
        } else {
          userBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        onChanged();
        return this;
      }
      /**
       * <code>.UbixUser user = 4;</code>
       */
      public Builder setUser(
          com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser.Builder builderForValue) {
        if (userBuilder_ == null) {
          user_ = builderForValue.build();
        } else {
          userBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        onChanged();
        return this;
      }
      /**
       * <code>.UbixUser user = 4;</code>
       */
      public Builder mergeUser(com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser value) {
        if (userBuilder_ == null) {
          if (((bitField0_ & 0x00000004) != 0) &&
            user_ != null &&
            user_ != com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser.getDefaultInstance()) {
            getUserBuilder().mergeFrom(value);
          } else {
            user_ = value;
          }
        } else {
          userBuilder_.mergeFrom(value);
        }
        if (user_ != null) {
          bitField0_ |= 0x00000004;
          onChanged();
        }
        return this;
      }
      /**
       * <code>.UbixUser user = 4;</code>
       */
      public Builder clearUser() {
        bitField0_ = (bitField0_ & ~0x00000004);
        user_ = null;
        if (userBuilder_ != null) {
          userBuilder_.dispose();
          userBuilder_ = null;
        }
        onChanged();
        return this;
      }
      /**
       * <code>.UbixUser user = 4;</code>
       */
      public com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser.Builder getUserBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getUserFieldBuilder().getBuilder();
      }
      /**
       * <code>.UbixUser user = 4;</code>
       */
      public com.atguigu.springcloud.other.proto.UbixUserModel.UbixUserOrBuilder getUserOrBuilder() {
        if (userBuilder_ != null) {
          return userBuilder_.getMessageOrBuilder();
        } else {
          return user_ == null ?
              com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser.getDefaultInstance() : user_;
        }
      }
      /**
       * <code>.UbixUser user = 4;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser, com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser.Builder, com.atguigu.springcloud.other.proto.UbixUserModel.UbixUserOrBuilder> 
          getUserFieldBuilder() {
        if (userBuilder_ == null) {
          userBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser, com.atguigu.springcloud.other.proto.UbixUserModel.UbixUser.Builder, com.atguigu.springcloud.other.proto.UbixUserModel.UbixUserOrBuilder>(
                  getUser(),
                  getParentForChildren(),
                  isClean());
          user_ = null;
        }
        return userBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:UbixBidRequest)
    }

    // @@protoc_insertion_point(class_scope:UbixBidRequest)
    private static final com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest();
    }

    public static com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<UbixBidRequest>
        PARSER = new com.google.protobuf.AbstractParser<UbixBidRequest>() {
      @java.lang.Override
      public UbixBidRequest parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        Builder builder = newBuilder();
        try {
          builder.mergeFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(builder.buildPartial());
        } catch (com.google.protobuf.UninitializedMessageException e) {
          throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(e)
              .setUnfinishedMessage(builder.buildPartial());
        }
        return builder.buildPartial();
      }
    };

    public static com.google.protobuf.Parser<UbixBidRequest> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<UbixBidRequest> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public com.atguigu.springcloud.other.proto.UbixBidRequestModel.UbixBidRequest getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_UbixBidRequest_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_UbixBidRequest_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\032proto/UbixBidRequest.proto\032\024proto/Ubix" +
      "User.proto\"R\n\016UbixBidRequest\022\022\n\nrequest_" +
      "id\030\001 \001(\t\022\023\n\013api_version\030\002 \001(\t\022\027\n\004user\030\004 " +
      "\001(\0132\t.UbixUserB:\n#com.atguigu.springclou" +
      "d.other.protoB\023UbixBidRequestModelb\006prot" +
      "o3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.atguigu.springcloud.other.proto.UbixUserModel.getDescriptor(),
        });
    internal_static_UbixBidRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_UbixBidRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_UbixBidRequest_descriptor,
        new java.lang.String[] { "RequestId", "ApiVersion", "User", });
    descriptor.resolveAllFeaturesImmutable();
    com.atguigu.springcloud.other.proto.UbixUserModel.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
