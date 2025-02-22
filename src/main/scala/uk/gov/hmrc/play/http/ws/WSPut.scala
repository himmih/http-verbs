/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.play.http.ws

import play.api.libs.json.{Json, Writes}
import uk.gov.hmrc.http.{CorePut, HeaderCarrier, HttpResponse, PutHttpTransport}

import scala.concurrent.Future

trait WSPut extends CorePut with PutHttpTransport with WSRequest {

  override def doPut[A](url: String, body: A)(implicit rds: Writes[A], hc: HeaderCarrier): Future[HttpResponse] = {
    doPut(url,body, Seq.empty[(String, String)])(rds, hc)
  }

  override def doPut[A](url: String, body: A, headers: Seq[(String, String)])(implicit rds: Writes[A], hc: HeaderCarrier): Future[HttpResponse] = {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    buildRequest(url).withHeaders(headers: _*).put(Json.toJson(body)).map(new WSHttpResponse(_))
  }

  override def doPutString(url: String, body: String, headers: Seq[(String, String)])(
    implicit hc: HeaderCarrier): Future[HttpResponse] = {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    buildRequest(url).withHeaders(headers: _*).put(body).map(new WSHttpResponse(_))
  }
}
