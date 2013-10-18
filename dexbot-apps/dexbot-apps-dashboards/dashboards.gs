function entressafras() {  
    return HtmlService
           .createTemplateFromFile('entressafras_template')
           .evaluate();
}