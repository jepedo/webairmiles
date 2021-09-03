select * from resource_bundles 
where id in (121,201);

UPDATE `resource_bundles` SET `value`='<div class="row-fluid">\n
<div span="1"></div>\n<div span="11">\n<div class="phone-call-out">\n<div class="phone pull-left"></div>\n
<p>Si vous avez besoin d\'aide, veuillez téléphoner au 1 888 877-1710 (du lundi au vendredi, entre 8 h et 19 h, HNE).</p>\n
</div>\n</div>\n</div>\n<div class="row-fluid">\n<div span="12">\n
<p class="trademark">* N’est pas disponible au Manitoba, ni au Saskatchewan;  Consultez les conditions générales ici.  Les adhérents peuvent gagner 1 mille de récompense AIR MILES par tranche de 20 $ de prime (taxes comprises).  Dans le cas des polices pour petites entreprises et pour les polices d\'assurance de bateaux de plaisance, les adhérents peuvent obtenir 1 mille de récompense AIR MILES pour chaque tranche de 20 $ de prime (taxes incluses), jusqu’à un maximum de 15 milles de récompense par mois. Toutes les offres de récompense AIR MILES sont assujetties aux conditions de la RSA et peuvent changer ou être retirées sans préavis. <sup>MD/MC</sup>Marque déposée/de commerce d\'AM Royalties Limited Partnership employée en vertu d\'une licence par LoyaltyOne, Co. et par Roins Financial Services Limited.</p>\n
<p class="trademark">* Seuls les titulaires de police sont admissibles aux milles de récompense AIR MILES. Les courtiers ne sont pas admissibles aux milles, à moins qu’ils ne soient titulaires de police. </p>\n</div>\n</div>' 
WHERE  `id`=201;

UPDATE `resource_bundles` SET `value`='<div class="row-fluid">\r\n<div span="1"></div>\r\n<div span="11">\r\n<div class="phone-call-out">\r\n<div class="phone pull-left"></div>\r\n<p>If you require assistance for RSA or WA, please call <strong>1-888-877-1710</strong><br />(Mon-Fri, 8am to 7pm EST)</p>\r\n</div>\r\n</div>\r\n</div>\r\n<div class="row-fluid">\r\n<div span="1"></div>\r\n<div span="11">\r\n<div class="phone-call-out">\r\n<div class="phone pull-left"></div>\r\n<p>If you require assistance for CNS, please call <strong>1-877-538-7558</strong><br />(Mon-Fri, 8:15am to 4:30pm PST)</p>     </div>\r\n</div>\r\n</div>\r\n<div class="row-fluid">\r\n<div span="12">\r\n
<p class="trademark">* Not available in Manitoba or Saskatchewan. Please see Terms & Conditions. Collectors can earn 1 AIR MILES reward mile for every $20 in premium (including taxes). On small-business and pleasure-craft policies, Collectors can earn 1 AIR MILES reward mile for every $20 in premium (including taxes), up to a maximum of 15 reward miles per month. All AIR MILES offers are subject to the Terms and Conditions of RSA, and may be changed or withdrawn without notice. &#174;&#8482;Trademarks of AM Royalties Limited Partnership used under license by LoyaltyOne, Co. and Roins Financial Services Limited. </p>\r\n
<p class="trademark">* Only policyholders are eligible to get AIR MILES Reward Miles. Brokers are not eligible to get Miles unless they are the policyholder.</p>\r\n</div>\r\n</div>' 
WHERE  `id`=121;
