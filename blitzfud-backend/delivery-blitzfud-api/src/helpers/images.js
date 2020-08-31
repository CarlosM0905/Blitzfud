function isValidExtension (file, mimetypes) {
    for (var validType of mimetypes){
        if (file.mimetype === validType){
            return true;
        }
    }
    return false;
}

module.exports = {
    isValidExtension
};