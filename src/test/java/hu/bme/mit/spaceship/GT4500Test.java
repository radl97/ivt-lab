package hu.bme.mit.spaceship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore store1;
  private TorpedoStore store2;

  @BeforeEach
  public void init(){
    store1 = mock(TorpedoStore.class);
    store2 = mock(TorpedoStore.class);
    this.ship = new GT4500(store1, store2);
  }

  private void arrangeNoFail() {
    when(store1.fire(1)).thenReturn(true);
    when(store2.fire(1)).thenReturn(true);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(store1.fire(1)).thenReturn(true);
    arrangeNoFail();

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(store1, times(1)).fire(1);
    verify(store2, times(0)).fire(1);
    assertTrue(result);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    arrangeNoFail();
    when(store1.isEmpty()).thenReturn(false);
    when(store2.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(store1, times(1)).fire(1);
    verify(store2, times(1)).fire(1);
    assertTrue(result);
  }

  @Test
  public void fireTorpedo_SingleTwice_Alternating(){
    // Arrange
    arrangeNoFail();
    when(store1.isEmpty()).thenReturn(false);
    when(store2.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = result && ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(store1, times(1)).fire(1);
    verify(store2, times(1)).fire(1);
    assertTrue(result);
  }

  @Test
  public void fireTorpedo_SingleTwice_PrimaryEmpty_Success(){
    // Arrange
    arrangeNoFail();
    when(store1.isEmpty()).thenReturn(true);
    when(store2.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = result && ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(store1, times(0)).fire(1);
    verify(store2, times(2)).fire(1);
    assertTrue(result);
  }

  @Test
  public void fireTorpedo_SingleTwice_SecondaryEmpty_Success(){
    // Arrange
    arrangeNoFail();
    when(store1.isEmpty()).thenReturn(false);
    when(store2.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = result && ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(store1, times(2)).fire(1);
    verify(store2, times(0)).fire(1);
    assertTrue(result);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFail_Fail(){
    // Arrange
    when(store1.fire(1)).thenReturn(false);
    when(store2.fire(1)).thenReturn(true);
    when(store1.isEmpty()).thenReturn(false);
    when(store2.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(store1, times(1)).fire(1);
    verify(store2, times(0)).fire(1);
    assertFalse(result);
  }

  @Test
  public void fireTorpedo_All_BothFail_Fail(){
    // Arrange
    when(store1.fire(1)).thenReturn(false);
    when(store2.fire(1)).thenReturn(false);
    when(store1.isEmpty()).thenReturn(false);
    when(store2.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(store1, times(1)).fire(1);
    verify(store2, times(1)).fire(1);
    assertFalse(result);
  }

  @Test
  public void fireTorpedo_Single_BothEmpty_Fail(){
    // Arrange
    arrangeNoFail();
    when(store1.isEmpty()).thenReturn(true);
    when(store2.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(store1, times(0)).fire(1);
    verify(store2, times(0)).fire(1);
    assertFalse(result);
  }

  @Test
  public void fireTorpedo_Single_TwoShotsOneTorpedo_Fail(){
    // Named after the famous video 2girls1cup
    // Arrange
    arrangeNoFail();
    when(store1.isEmpty()).thenReturn(false).thenReturn(true);
    when(store2.isEmpty()).thenReturn(true).thenReturn(true);

    // Act
    boolean firstResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondResult = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(store1, times(1)).fire(1);
    verify(store2, times(0)).fire(1);
    assertTrue(firstResult);
    assertFalse(secondResult);
  }

  @Test
  public void fireTorpedo_All_BothEmpty_Fail(){
    // Named after the famous 2girls1cup
    // Arrange
    arrangeNoFail();
    when(store1.isEmpty()).thenReturn(true);
    when(store2.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(store1, times(0)).fire(1);
    verify(store2, times(0)).fire(1);
    assertFalse(result);
  }

  @Test
  public void fireTorpedo_Invalid_Fail(){
    // Arrange
    arrangeNoFail();
    when(store1.isEmpty()).thenReturn(false);
    when(store2.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.INVALID);

    // Assert
    assertFalse(result);
  }

}
