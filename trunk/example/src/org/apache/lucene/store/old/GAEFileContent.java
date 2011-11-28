package org.apache.lucene.store.old;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;

/**
 * GAEFileContent stands for (part of) the index file's byte content.
 * 
 * $Id: GAEFileContent.java 25 2009-09-14 02:10:51Z lhelper $
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class GAEFileContent {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long cid;

  @Persistent
  private Long fileId;

  @Persistent
  private int segmentNo;

  @Persistent
  private Long segmentLength;

  @Persistent
  private Blob content;

  public Long getId() {
    return cid;
  }

  public void setId(Long id) {
    this.cid = id;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public int getSegmentNo() {
    return segmentNo;
  }

  public void setSegmentNo(int segmentNo) {
    this.segmentNo = segmentNo;
  }

  public Long getSegmentLength() {
    return segmentLength;
  }

  public void setSegmentLength(Long segmentLength) {
    this.segmentLength = segmentLength;
  }

  public Blob getContent() {
    return content;
  }

  public void setContent(Blob content) {
    this.content = content;
  }
}
