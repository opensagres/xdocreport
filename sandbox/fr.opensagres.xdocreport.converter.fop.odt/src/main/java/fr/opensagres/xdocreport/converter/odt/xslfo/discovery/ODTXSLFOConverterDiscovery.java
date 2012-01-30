/**
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
package fr.opensagres.xdocreport.converter.odt.xslfo.discovery;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.discovery.IConverterDiscovery;
import fr.opensagres.xdocreport.converter.odt.xslfo.ODTXSLFOConverter;
import fr.opensagres.xdocreport.core.document.DocumentKind;

public class ODTXSLFOConverterDiscovery
    implements IConverterDiscovery
{

    public String getId()
    {
        return "ODT2FOViaXSL";
    }

    public String getDescription()
    {
        return "Convert ODT 2 FO via XSL";
    }

    public String getFrom()
    {
        return DocumentKind.ODT.name();
    }

    public String getTo()
    {
        return ConverterTypeTo.FO.name();
    }

    public String getVia()
    {
        return ConverterTypeVia.XSL.name();
    }

    public IConverter getConverter()
    {
        return ODTXSLFOConverter.getInstance();
    }

}
