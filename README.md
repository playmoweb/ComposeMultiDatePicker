# MultiDatePicker

A customizable date picker which support single date picker and range date picker.

# Example

![Basis](./doc/multiDatePicker.png)

# Usage

By selecting a day on the first click, it will set the startDate value.
If the second selection is before the first one, it will set the startDate value to the second picked day.
If the second selection is after the first one, it will set the endDate value.
Clicking a third time will reset the startDate and endDate values, and assign the selected date to startDate.

## Attributes
| Attributes  |         Type          |          Example Value           |                              Description                              |
|:-----------:|:---------------------:|:--------------------------------:|:---------------------------------------------------------------------:|
|   minDate   |         Date?         |      Calendar.getInstance()      |            Minimum day that can be selected in the picker             |
|   maxDate   |         Date?         |      Calendar.getInstance()      |            Maximum day that can be selected in the picker             |
|  startDate  |  MutableState<Date?>  |       mutableStateOf(null)       | First date selected in picker (represent the start date of the range) |
|   endDate   |  MutableState<Date?>  |       mutableStateOf(null)       | Second date selected in picker (represent the end date of the range)  |
|   colors    | MultiDatePickerColors | MultiDatePickerColors.defaults() |                Theme of colors used by the datePicker                 |
| cardRadius  |          Dp           |               5.dp               |                         Radius of the picker                          |
| onStartDate |    (Date?) -> Unit    |  (date: Date?) =>   print(date)  | Triggered when startDate value changed (by picking it in the picker)  |
|  onEndDate  |    (Date?) -> Unit    |  (date: Date?) =>   print(date)  |  Triggered when endDate value changed (by picking it in the picker)   |