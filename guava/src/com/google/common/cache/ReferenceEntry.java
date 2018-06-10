/*
 * Copyright (C) 2009 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.common.cache;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.cache.LocalCache.ValueReference;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * An entry in a reference map.
 *
 * 一个map里的引用数据项
 * <p>Entries in the map can be in the following states:
 *
 * map里的数据项可能包含以下几种状态：
 *
 *   有效：
 *     1. 活得；有效的key/value被设置
 *     2. 加载状态：加载状态是一种等待加载完成。
 * <p>Valid:
 *
 * <ul>
 *   <li>Live: valid key/value are set
 *   <li>Loading: loading is pending
 * </ul>
 *
 * <p>Invalid:
 *
 *  无效的数据项：
 *
 *    过期的：
 *    被垃圾收集器收集的，部分key/value被垃圾收集器收集。但是至今没有cleaned up
 *    被回收，等待cleanup或者重用。
 *
 * <ul>
 *   <li>Expired: time expired (key/value may still be set)
 *   <li>Collected: key/value was partially collected, but not yet cleaned up
 *   <li>Unset: marked as unset, awaiting cleanup or reuse
 * </ul>
 */
@GwtIncompatible
interface ReferenceEntry<K, V> {
  /** Returns the value reference from this entry. */

  /**值引用*/
  ValueReference<K, V> getValueReference();

  /** Sets the value reference for this entry. */
  /**设置值引用*/
  void setValueReference(ValueReference<K, V> valueReference);

  /** Returns the next entry in the chain. */
  /**返回链路中的下一个引用*/
  @Nullable
  ReferenceEntry<K, V> getNext();

  /** Returns the entry's hash. */
  int getHash();

  /** Returns the key for this entry. */
  /**返回此数据项的Key*/
  @Nullable
  K getKey();

  /*
   * Used by entries that use access order. Access entries are maintained in a doubly-linked list.
   * New entries are added at the tail of the list at write time; stale entries are expired from
   * the head of the list.
   */

  /** Returns the time that this entry was last accessed, in ns. */
  /**返回数据项的最后一次访问时间*/
  long getAccessTime();

  /** Sets the entry access time in ns. */
  void setAccessTime(long time);

  /** Returns the next entry in the access queue. */
  ReferenceEntry<K, V> getNextInAccessQueue();

  /** Sets the next entry in the access queue. */
  void setNextInAccessQueue(ReferenceEntry<K, V> next);

  /** Returns the previous entry in the access queue. */
  ReferenceEntry<K, V> getPreviousInAccessQueue();

  /** Sets the previous entry in the access queue. */
  void setPreviousInAccessQueue(ReferenceEntry<K, V> previous);

  /*
   * Implemented by entries that use write order. Write entries are maintained in a doubly-linked
   * list. New entries are added at the tail of the list at write time and stale entries are
   * expired from the head of the list.
   *
   * 实体被维护在双端队列中。新的实体被添加在集合的尾部在写时。实体过期时从头部开始
   */

  /** Returns the time that this entry was last written, in ns. */
  long getWriteTime();

  /** Sets the entry write time in ns. */
  void setWriteTime(long time);

  /** Returns the next entry in the write queue. */
  ReferenceEntry<K, V> getNextInWriteQueue();

  /** Sets the next entry in the write queue. */
  void setNextInWriteQueue(ReferenceEntry<K, V> next);

  /** Returns the previous entry in the write queue. */
  ReferenceEntry<K, V> getPreviousInWriteQueue();

  /** Sets the previous entry in the write queue. */
  void setPreviousInWriteQueue(ReferenceEntry<K, V> previous);
}
