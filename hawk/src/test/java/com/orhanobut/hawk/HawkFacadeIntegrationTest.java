package com.orhanobut.hawk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class HawkFacadeIntegrationTest {

  @Before public void setUp() {
    Hawk.init(RuntimeEnvironment.application).build();
  }

  @After public void tearDown() {
    if (Hawk.isBuilt()) {
      Hawk.deleteAll();
    }
    Hawk.destroy();
  }

  @Test public void testSingleItem() {
    Hawk.put("boolean", true);

    Object hawkObject = Hawk.get("boolean");
    assertThat(hawkObject).isEqualTo(true);

    Hawk.put("string", "string");

    hawkObject = Hawk.get("string");
    assertThat(hawkObject).isEqualTo("string");

    Hawk.put("float", 1.5f);

    hawkObject = Hawk.get("float");
    assertThat(hawkObject).isEqualTo(1.5f);

    Hawk.put("integer", 10);

    hawkObject = Hawk.get("integer");
    assertThat(hawkObject).isEqualTo(10);

    Hawk.put("char", 'A');

    hawkObject = Hawk.get("char");
    assertThat(hawkObject).isEqualTo('A');

    Hawk.put("object", new FooBar());
    FooBar fooBar = Hawk.get("object");

    assertThat(fooBar).isNotNull();
    assertThat(fooBar.getName()).isEqualTo("hawk");

    assertThat(Hawk.put("innerClass", new FooBar.InnerFoo())).isTrue();
    FooBar.InnerFoo innerFoo = Hawk.get("innerClass");
    assertThat(innerFoo).isNotNull();
    assertThat(innerFoo.getName()).isEqualTo("hawk");
  }

  @Test public void testSingleItemDefault() {
    boolean result = Hawk.get("tag", true);
    assertThat(result).isEqualTo(true);
  }

  @Test public void testCount() {
    Hawk.deleteAll();
    String value = "test";
    Hawk.put("tag", value);
    Hawk.put("tag1", value);
    Hawk.put("tag2", value);
    Hawk.put("tag3", value);
    Hawk.put("tag4", value);

    assertThat(Hawk.count()).isEqualTo(5);
  }

  @Test public void testDeleteAll() {
    String value = "test";
    Hawk.put("tag", value);
    Hawk.put("tag1", value);
    Hawk.put("tag2", value);

    Hawk.deleteAll();

    assertThat(Hawk.count()).isEqualTo(0);
  }

  @Test public void testDelete() {
    Hawk.deleteAll();
    String value = "test";
    Hawk.put("tag", value);
    Hawk.put("tag1", value);
    Hawk.put("tag2", value);

    Hawk.delete("tag");

    String result = Hawk.get("tag");

    assertThat(result).isNull();
    assertThat(Hawk.count()).isEqualTo(2);
  }

  @Test public void testContains() {
    Hawk.put("key", "value");

    assertThat(Hawk.contains("key")).isTrue();
  }


  @Test public void testHugeData() {
    for (int i = 0; i < 100; i++) {
      Hawk.put("" + i, "" + i);
    }
    assertThat(true).isTrue();
  }

}
