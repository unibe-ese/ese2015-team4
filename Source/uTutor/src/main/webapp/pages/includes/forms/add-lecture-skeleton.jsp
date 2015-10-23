<label>
	<form:input path="lecture" id="field-lecture" placeholder="Lecture"/>
   	<form:errors path="lecture" element="div" class="error"/>
</label>

<label>
	<div class="controls">
	Grade: 
       		<form:select path="grade">
       			<form:option value="4">4</form:option>
       			<form:option value="4.5">4.5</form:option>
       			<form:option value="5">5</form:option>
       			<form:option value="5.5">5.5</form:option>
       			<form:option value="6">6</form:option>
       		</form:select>
   			<form:errors path="grade" element="div" class="error"/>
	</div>
</label>