/*
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com>, Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
function submitForm(dispatch, target) {
	var dispatchHTMLInputHidden = document.getElementById('dispatch');
	dispatchHTMLInputHidden.value = dispatch;
	var form = dispatchHTMLInputHidden.form;
	if (target != null) {
		form.target = target;
	}
	form.submit();
}

function processReport(baseURL, fieldTextarea) {
	var previewFrame = document.getElementById('previewFrame');
	if (previewFrame == null) {
		return true;
	}

	// Is textarea changed?
	if (fieldTextarea != null) {
		var oldValue = fieldTextarea.getAttribute('oldValue');
		var newValue = fieldTextarea.value;
		if (oldValue == newValue) {
			return true;
		}
		// textarea change, iframe must be refreshed.
		fieldTextarea.setAttribute('oldValue', newValue)
	}

	// Build URL
	var url = baseURL;
	var dataModel = document.getElementById('dataModel');
	var textareas = dataModel.getElementsByTagName('textarea');
	for ( var i = 0; i < textareas.length; i++) {
		var textarea = textareas.item(i);
		url += '&';
		url += textarea.name;
		url += '=';
		url += textarea.value.replace('&', '%26').replace('\n', '%0D%0A');
	}
	previewFrame.src = url;
	return true;
}

var firstRow = null;
function add(baseURL, tableId) {
	var table = document.getElementById(tableId);
	if (table == null) {
		return;
	}

	var newTR = null;
	if (firstRow != null) {
		// At this step, no row in the table , use the global first row.
		newTR = firstRow;
		firstRow = null;
	} else {
		// get last row of the table
		var rows = table.rows;
		var lastRow = rows[rows.length - 1];

		// Copy last row line of the data model table
		newTR = lastRow.cloneNode(true)
	}

	// Add the copied line to the table
	table.appendChild(newTR);

	// Refresh iframe preview
	processReport(baseURL, null);
}

function remove(baseURL, tableId) {
	var table = document.getElementById(tableId);
	if (table == null) {
		return;
	}

	// get last row of the table
	var rows = table.rows;
	var index = rows.length - 1;
	if (index < 2) {
		// No row to delete (row1 = a href Add+Remove), row2 = table header
		return;
	}

	if (index == 2) {
		// One row, store it in the global variable
		firstRow = rows[index];
	}
	// Remove last row
	table.deleteRow(index);

	// Refresh iframe preview
	processReport(baseURL, null);
}