var environments = {
  tealium: {
    env: '',
    profile: ''
  },
  site: ''
};

if (/^(http|https)\:\/\/([\da-z\.-]+)\.(sit\.|qa\.)rsabase.com/.test(document.URL) || /localhost/.test(document.URL) || /\.dev/.test(document.URL)) {
  environments.tealium.env = 'dev'; 
}
/*else if (/^(http|https)\:\/\/([\da-z\.-]+)\.uat\.rsabase.com/.test(document.URL)) {
  environments.tealium.env = 'qa';
}*/
else {
  environments.tealium.env = 'prod';
}

if (/^(http|https)\:\/\/(rsagroup\.)/.test(document.URL)) {
  environments.tealium.profile = 'rsagroup'; 
  environments.site = 'rsagroup.ca';
}
else if (/^(http|https)\:\/\/(johnson\.)/.test(document.URL)) {
  environments.tealium.profile = 'johnson';
  environments.site = 'johnson.ca';
}
