# MultiDatePicker

A customizable date picker which support single date picker and range date picker.

# How to install

Add the following to your project level `build.gradle`:

```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependancy to your module `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.playmoweb:MultiDatePicker:1.0.1'
}
```

# Example

![Basis](./doc/multiDatePicker.png)

# Usage

1. By selecting a day on the first click, it will set the startDate value.
2. If the second selection is before the first one, it will set the startDate value to the second picked day. 
If the second selection is after the first one, it will set the endDate value.
3. Clicking a third time will reset the startDate and endDate values, and assign the selected date to startDate.
4. Clicking on the selected date if only one date is selected will reset the selected date.

## Attributes
| Attributes  |         Type          |          Example Value           |                              Description                              |
|:-----------:|:---------------------:|:--------------------------------:|:---------------------------------------------------------------------:|
|   minDate   |         Date?         |      Calendar.getInstance()      |            Minimum day that can be selected in the picker             |
|   maxDate   |         Date?         |      Calendar.getInstance()      |            Maximum day that can be selected in the picker             |
|  startDate  |  MutableState<Date?>  |       mutableStateOf(null)       | First date selected in picker (represent the start date of the range) |
|   endDate   |  MutableState<Date?>  |       mutableStateOf(null)       | Second date selected in picker (represent the end date of the range)  |
|   colors    | MultiDatePickerColors | MultiDatePickerColors.defaults() |                Theme of colors used by the datePicker                 |
| cardRadius  |          Dp           |               5.dp               |                         Radius of the picker                          |