function doGet() {
  return HtmlService
      .createTemplateFromFile('entressafras_template')
      .evaluate();
}